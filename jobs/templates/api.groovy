return { Map config ->
    job(config.jobName) {
        description(config.description)

        parameters {
            stringParam('BRANCH', config.defaultBranch ?: 'product', '브랜치 : [브랜치명]\n태그 :  refs/tags/[태그번호]')
            stringParam('ENV', config.env ?: 'develop', 'resources-[ENV/[SITE]')
            stringParam('SITE', config.site ?: 'common', 'resources-[ENV/[SITE]')
            booleanParam('SKIP_TEST', false, '단위 테스트 생략 여부')
        }

        logRotator {
            daysToKeep(1)
            numToKeep(3)
        }

        scm {
            git {
                remote {
                    url('git@github.com:pntbiz1/pntbiz-indoorplus-api.git')
                    credentials('ssh-pntbiz-indoorplus-api')
                }
                branch('${BRANCH}')
            }
        }

        jdk('JDK8')

        steps {
            maven {
                goals("clean install -Dmaven.test.skip=\${SKIP_TEST} -Denvironment=\${ENV} -Dsite=\${SITE}")
                pom('./pntbiz-api/pom.xml')
                mavenInstallation('maven-3.5.3')
            }

            configure { project ->
                def builders = project / 'builders'
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
            }

            shell("""
                echo "----------- remove build file -----------"
                sleep 3
                rm -rf ${config.cleanup.workspace}/*
                rm -rf ${config.cleanup.workspace}/.git
                rm -rf ${config.cleanup.deployTarget}/*
            """.stripIndent())
        }

        publishers {
            slackNotifier {
                startNotification(true)
                notifySuccess(true)
                notifyEveryFailure(true)
                includeFailedTests(true)
            }
        }
    }
}