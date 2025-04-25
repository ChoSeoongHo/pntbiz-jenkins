return { Map config ->
    job(config.jobName) {
        description(config.description)

        logRotator {
            daysToKeep(1)
            numToKeep(3)
        }

        parameters {
            def defaultParams = [
                    [type: 'string', name: 'BRANCH', defaultValue: config.defaultBranch ?: 'develop', description: '']
            ]

            def mergedParams = defaultParams + (config.parameters ?: [])

            mergedParams.each { param ->
                switch (param.type) {
                    case 'string':
                        stringParam(param.name, param.defaultValue ?: '', param.description ?: '')
                        break
                    case 'choice':
                        choiceParam(param.name, param.choices ?: [], param.description ?: '')
                        break
                    case 'boolean':
                        booleanParam(param.name, param.defaultValue ?: false, param.description ?: '')
                        break
                }
            }
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

        jdk(config.jdk ?: 'JDK17')

        steps {
            configure { project ->
                def builders = project / 'builders'

                // STEP 1: 서버 상태 확인
                builders << 'hudson.tasks.Shell' {
                    command("""
                        SERVER_NAME=${config.serverKey}
                        SERVER_INSTANCE_NO=${config.instanceNo}

                        echo "========== Server Status Check: \$SERVER_NAME =========="
                        serverStatusCheck=\$(ncloud vserver getServerInstanceList | grep "\$SERVER_NAME" -A 15 | grep RUN)
                        if [ -z "\$serverStatusCheck" ]; then
                          echo "> Server is not running. Starting the server..."
                          ncloud vserver startServerInstances --serverInstanceNoList \$SERVER_INSTANCE_NO
                          echo "> Waiting for the server to be fully up..."
                          sleep 120
                        else
                          echo "> Server is already running."
                        fi
                        echo "========== Server Status Check Done =========="
                    """.stripIndent())
                }

                // STEP 2: Gradle 빌드
                builders << 'hudson.plugins.gradle.Gradle' {
                    tasks(config.gradleTasks ?: 'clean build -x test')
                    gradleName(config.gradleName ?: 'gradle-8.10.2')
                    useWrapper(false)
                    if (config.buildFile) {
                        buildFile(config.buildFile)
                    }
                }

                // STEP 3: 아티팩트 생성 및 압축
                builders << 'hudson.tasks.Shell' {
                    command(config.packagingScript.stripIndent())
                }

                // STEP 4: Ansible 배포
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

                // STEP 5: 정리
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
