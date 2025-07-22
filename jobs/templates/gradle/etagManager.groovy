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
                packagingScript: """
                    ZIP_NAME='etag-manager.zip'
                    DEPLOY_FILE_NAME='indoorplus-etag-management.jar'
                    DEPLOY_DIR_NAME='etag'
                    
                    echo "----------- Make temp directory -----------"
                    mkdir -p \${DEPLOY_DIR_NAME}
                    mkdir -p \${DEPLOY_DIR_NAME}/pem/\${PEM_VERSION}
                    mkdir -p \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- cp file -----------"
                    cp pntbiz-etag-management/build/libs/*.jar \${DEPLOY_DIR_NAME}
                    cp deploy-conf/sh/dev/*.sh \${DEPLOY_DIR_NAME}
                    cp deploy-conf/conf/dev/* \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- Make zip file -----------"
                    cd \${DEPLOY_DIR_NAME}
                    #mv *.jar \${DEPLOY_FILE_NAME}
                    zip -r \${DEPLOY_DIR_NAME} *
        """.stripIndent()
        ])
    }
}
