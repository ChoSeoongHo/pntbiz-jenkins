package jobs.templates.gradle

return { gradleTemplate ->
    return { Map baseConfig ->
        gradleTemplate.delegate = delegate
        gradleTemplate.resolveStrategy = DELEGATE_FIRST
        baseConfig.defaultBranch = 'master';

        gradleTemplate(baseConfig + [
                repoUrl        : 'git@github.com:pntbiz1/pntbiz-indoorplus-oauth.git',
                credentialsId  : 'ssh-pntbiz-indoorplus-oauth',
                parameters     : [
                        [type: 'choice', name: 'PEM_VERSION', choices: ['v1', 'v2', 'v3', 'v4', 'v5'], description: ''],
                ],
                gradleTasks    : 'clean build -x test',
                gradleName     : 'gradle-7.2',
                jdk            : 'JDK8',
                packagingScript: """
                    ZIP_NAME='oauth.zip'
                    DEPLOY_FILE_NAME='indoorplus-oauth-server.jar'
                    DEPLOY_DIR_NAME='oauth'
                    
                    echo "----------- Make temp directory -----------"
                    mkdir -p \${DEPLOY_DIR_NAME}
                    mkdir -p \${DEPLOY_DIR_NAME}/pem/\${PEM_VERSION}
                    mkdir -p \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- cp file -----------"
                    cp pntbiz-indoorplus-oauth-api/build/libs/*.jar \${DEPLOY_DIR_NAME}
                    cp deploy-conf/sh/dev/*.sh \${DEPLOY_DIR_NAME}
                    cp deploy-conf/conf/dev/* \${DEPLOY_DIR_NAME}/conf
                    cp deploy-conf/pem/dev/\${PEM_VERSION}/* \${DEPLOY_DIR_NAME}/pem/\${PEM_VERSION}
                    
                    echo "----------- Make zip file -----------"
                    cd \${DEPLOY_DIR_NAME}
                    #mv *.jar \${DEPLOY_FILE_NAME}
                    zip -r \${DEPLOY_DIR_NAME} *
        """.stripIndent()
        ])
    }
}
