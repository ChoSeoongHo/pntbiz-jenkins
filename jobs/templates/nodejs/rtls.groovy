package jobs.templates.nodejs

return { Map config ->
    job(config.jobName) {
        description(config.description)

        logRotator {
            daysToKeep(1)
            numToKeep(1)
        }

        parameters {
            stringParam('BRANCH', config.defaultBranch ?: 'v12-master', '')
            stringParam('VERSION', config.version ?: 'v12.18.3', '')
            stringParam('ENV', config.env ?: 'site/common', '')
            choiceParam('NODE_VERSION', ['v14.19.1', 'v20.18.3', 'v22.14.0'], 'Node 버전 설정')
        }

        scm {
            git {
                remote {
                    url('git@github.com:pntbiz1/pntbiz-private-rtls-server.git')
                    credentials('ssh-pntbiz-rtls')
                }
                branch('${BRANCH}')
            }
        }

        steps {
            configure { project ->
                def builders = project / 'builders'

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

                builders << 'hudson.tasks.Shell' {
                    command("""
                        export NVM_DIR="/var/lib/jenkins/.nvm"
                        [ -s "\$NVM_DIR/nvm.sh" ] && \\. "\$NVM_DIR/nvm.sh"

                        DEPLOY_DIR_NAME=rtls

                        echo "----------- Change Node Version -----------"
                        nvm use \${NODE_VERSION}
                        node -v

                        echo "----------- Make zip file -----------"
                        cd RSSI-No-Queue
                        rm -rf node_modules
                        cd ..
                        /usr/local/bin/npm install
                        mv node_modules ./RSSI-No-Queue
                        cd RSSI-No-Queue
                        /usr/local/bin/node obfuscator.js
                        cd ..
                        rm package-lock.json
                        rm -rf doc
                        cd RSSI-No-Queue
                        rm -rf config.json
                        rm -rf logger/winstonLogger.js
                        cd ..

                        echo "----------- cp file -----------"
                        cp deploy-conf/conf/\${ENV}/config.json RSSI-No-Queue
                        cp deploy-conf/conf/\${ENV}/winstonLogger.js RSSI-No-Queue/logger
                        rm -rf deploy-conf
                        rm -rf rtls_data

                        zip -r \${DEPLOY_DIR_NAME} *

                        nvm use default
                    """.stripIndent())
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
