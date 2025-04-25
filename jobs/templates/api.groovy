return { Map config ->
    job(config.jobName) {
        description(config.description)

        parameters {
            stringParam('BRANCH', config.defaultBranch ?: 'product', '브랜치 : [브랜치명]\n태그 :  refs/tags/[태그번호]')
            stringParam('ENV', config.env ?: 'develop', 'resources-[ENV]/[SITE]')
            stringParam('SITE', config.site ?: 'common', 'resources-[ENV]/[SITE]')
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
            configure { project ->
                def builders = project / 'builders'

                builders << 'hudson.tasks.Shell' {
                    command("""
                    SERVER_NAME=${serverName}
                    SERVER_INSTANCE_NO=${instanceNo}
                    
                    echo "========== Server Status Check: \${SERVER_NAME} =========="
                    serverStatusCheck=\$(ncloud vserver getServerInstanceList | grep "\${SERVER_NAME}" -A 15 | grep RUN)
                    if [ -z "\$serverStatusCheck" ]; then
                      echo "> Server is not running. Starting the server..."
                      ncloud vserver startServerInstances --serverInstanceNoList \${SERVER_INSTANCE_NO}
                      echo "> Waiting for the server to be fully up..."
                      sleep 120
                    else
                      echo "> Server is already running."
                    fi
                    echo "========== Server Status Check Done =========="
                    """.stripIndent())
                }

                builders << 'hudson.tasks.Maven' {
                    targets("clean install -Dmaven.test.skip=\${SKIP_TEST} -Denvironment=\${ENV} -Dsite=\${SITE}")
                    mavenName('maven-3.5.3')
                    usePrivateRepository(false)
                    rootPOM('./pntbiz-api/pom.xml')
                }

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
                includeFailedTests(true)
            }
        }
    }
}