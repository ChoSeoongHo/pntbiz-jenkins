return { Map config ->
    job(config.jobName) {
        description(config.description)

        parameters {
            stringParam('BRANCH', config.defaultBranch ?: 'pjt-02-develop', '브랜치 : [브랜치명]\n태그 :  refs/tags/[태그번호]')
            stringParam('VERSION', config.version ?: 'v12', '')
            stringParam('CONFIG', config.config ?: 'dev/common', 'config 하위 폴더 넣기')
            stringParam('SHELL', config.shell ?: 'dev', 'sh 하위 폴더 넣기')
            choiceParam('NODE_VERSION', ['v14.19.1', 'v20.18.3', 'v22.14.0'], 'Node 버전 설정')
        }

        scm {
            git {
                remote {
                    url('git@github.com:pntbiz1/pntbiz-indoorplus-socket.git')
                    credentials('ssh-pntbiz-indoorplus-socket')
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

                        DEPLOY_DIR_NAME=socket

                        echo "----------- Change Node Version -----------"
                        nvm use \${NODE_VERSION}
                        node -v

                        echo "----------- Make temp directory -----------"
                        rm -rf \$DEPLOY_DIR_NAME
                        mkdir -p \$DEPLOY_DIR_NAME/config/\${CONFIG}
                        mkdir -p pntbiz-socket/service-product
                        mkdir -p pntbiz-socket/modules

                        echo "----------- cp file -----------"
                        cp README.md \$DEPLOY_DIR_NAME
                        cp -R pntbiz-socket/config/\${CONFIG}/* \$DEPLOY_DIR_NAME/config/\${CONFIG}
                        cp pntbiz-socket/config/config.js \$DEPLOY_DIR_NAME/config/
                        cp pntbiz-socket/sh/\${SHELL}/*.sh \$DEPLOY_DIR_NAME
                        cp pntbiz-socket/*.js \$DEPLOY_DIR_NAME
                        cp pntbiz-socket/*.json \$DEPLOY_DIR_NAME
                        cp -R pntbiz-socket/service-product \$DEPLOY_DIR_NAME
                        cp -R pntbiz-socket/modules \$DEPLOY_DIR_NAME

                        echo "----------- Make zip file -----------"
                        cd \$DEPLOY_DIR_NAME
                        /usr/local/bin/npm cache clean --force
                        /usr/local/bin/npm install
                        zip -r \$DEPLOY_DIR_NAME *
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
