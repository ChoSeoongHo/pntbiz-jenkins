package jobs.templates.maven

return { Map config ->
    job(config.jobName) {
        description(config.description)

        parameters {
            stringParam('BRANCH', config.defaultBranch ?: 'develop', '브랜치 : [브랜치명]\n태그 :  refs/tags/[태그번호]')
            stringParam('ENV', config.env ?: 'develop', 'resources-[ENV]/[SITE]')
            stringParam('SITE', config.site ?: 'common', 'resources-[ENV]/[SITE]')
            if (config.withSkipTestParam) {
                def skipTestDefault = config.containsKey('runTestBeforeBuild') ? !config.runTestBeforeBuild : true
                booleanParam('SKIP_TEST', skipTestDefault, '단위 테스트 생략 여부')
            }
        }

        logRotator {
            daysToKeep(1)
            numToKeep(3)
        }

        scm {
            git {
                remote {
                    url(config.repoUrl)
                    credentials(config.credentialsId)
                }
                branch('${BRANCH}')
            }
        }

        jdk(config.jdk ?: 'JDK8')

        steps {
            configure { project ->
                def builders = project / 'builders'

                // Step 1: Maven Build
                builders << 'hudson.tasks.Maven' {
                    targets(config.mavenGoals ?: "clean install -Dmaven.test.skip=\${SKIP_TEST} -Denvironment=\${ENV} -Dsite=\${SITE}")
                    pom(config.pomPath ?: './pom.xml')
                    mavenName(config.mavenName ?: 'maven-3.5.3')
                    usePrivateRepository(false)
                }

                // Step 2: Ansible 배포
                builders << 'org.jenkinsci.plugins.ansible.AnsiblePlaybookBuilder' {
                    playbook(config.playbook)
                    ansibleName('ANSIBLE_HOME')
                    forks(5)
                    unbufferedOutput(true)
                    additionalParameters("-i ${config.inventory}")

                    extraVars {
                        config.extraVars.each { k, v ->
                            'org.jenkinsci.plugins.ansible.ExtraVar' {
                                key(k)
                                value(v)
                                hidden(false)
                            }
                        }
                    }
                }

                // Step 3: 정리
                builders << 'hudson.tasks.Shell' {
                    command("""
                        echo "----------- Clean-Up Workspace -----------"
                        sleep 3
                        rm -rf ${config.cleanup.workspace}/*
                        rm -rf ${config.cleanup.workspace}/.git
                        rm -rf ${config.cleanup.deployTarget}/*
                    """.stripIndent())
                }
            }
        }

        publishers {
            slackNotifier {
                startNotification(true)
                notifySuccess(true)
                notifyEveryFailure(true)
                includeFailedTests(false)
                commitInfoChoice('NONE')
            }
        }
    }
}
