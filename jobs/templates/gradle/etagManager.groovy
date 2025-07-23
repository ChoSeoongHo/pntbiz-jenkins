package jobs.templates.gradle

return { gradleTemplate ->
    return { Map baseConfig ->
        gradleTemplate.delegate = delegate
        gradleTemplate.resolveStrategy = DELEGATE_FIRST

        gradleTemplate(baseConfig + [
                repoUrl        : 'git@github.com:pntbiz1/pntbiz-etag-management.git',
                credentialsId  : 'pntbiz-etag-management',
                gradleTasks    : 'clean build -x test',
                gradleName     : 'gradle-7.2',
                jdk            : 'JDK8',
                defaultBranch  : 'develop',
                preScripts     : [
                        """
                        echo "----------- Switch Node Version to v22.14.0 -----------"
                        export NVM_DIR="/var/lib/jenkins/.nvm"
                        [ -s "\$NVM_DIR/nvm.sh" ] && \\. "\$NVM_DIR/nvm.sh"
                        nvm use v22.14.0
                        node -v
                        echo "----------- Disable CI mode -----------"
                        export CI=false
                        echo "----------- Build React App -----------"
                        npm --prefix ./src/main/frontend install
                        npm --prefix ./src/main/frontend run build
                        """
                ],
                postScripts    : [
                        """
                        echo "----------- Revert Node version to default -----------"
                        export NVM_DIR="/var/lib/jenkins/.nvm"
                        [ -s "\\$NVM_DIR/nvm.sh" ] && \\\\. "\\$NVM_DIR/nvm.sh"
                        nvm use default
                        """
                ],
                packagingScript: """
                    ZIP_NAME='etag.zip'
                    DEPLOY_FILE_NAME='indoorplus-etag-management.jar'
                    DEPLOY_DIR_NAME='etag'
                    
                    echo "----------- Make temp directory -----------"
                    mkdir -p \${DEPLOY_DIR_NAME}
                    mkdir -p \${DEPLOY_DIR_NAME}/pem/\${PEM_VERSION}
                    mkdir -p \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- cp file -----------"
                    cp build/libs/*.jar \${DEPLOY_DIR_NAME}
                    cp deploy-conf/sh/dev/*.sh \${DEPLOY_DIR_NAME}
                    cp deploy-conf/conf/dev/* \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- Make zip file -----------"
                    cd "\${DEPLOY_DIR_NAME}"
                    zip -r "../\${DEPLOY_DIR_NAME}.zip" *
        """.stripIndent()
        ])
    }
}
