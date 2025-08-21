package jobs.templates.gradle

return { gradleTemplate ->
    return { Map baseConfig ->
        gradleTemplate.delegate = delegate
        gradleTemplate.resolveStrategy = DELEGATE_FIRST

        return gradleTemplate(baseConfig + [
                repoUrl        : 'git@github.com:pntbiz1/indoorplus-smart-sensing-core-api.git',
                credentialsId  : 'indoorplus-smart-sensing-core-api',
                parameters     : [
                        [type: 'string', name: 'ENV', defaultValue: 'develop', description: ''],
                        [type: 'string', name: 'SITE', defaultValue: 'common', description: '']
                ],
                gradleTasks    : 'clean build -x test -Pprofile=${ENV} -Psite=${SITE}',
                gradleName     : 'gradle-8.10.2',
                jdk            : 'JDK17',
                buildFile: 'core-api/build.gradle',
                packagingScript: """
            DEPLOY_FILE_NAME='api.jar'
            DEPLOY_DIR_NAME='pntbiz_core_api'
            
            echo "----------- Make temp directory -----------"
            mkdir -p \${DEPLOY_DIR_NAME}
            mkdir -p \${DEPLOY_DIR_NAME}/conf/pem.v1
            
            echo "----------- cp file -----------"
            cp core-api/build/libs/*.jar \${DEPLOY_DIR_NAME}
            cp deploy-conf/sh/\${ENV}/\${SITE}/*.sh \${DEPLOY_DIR_NAME}
            cp core-api/src/main/resources/resources-\${ENV}/\${SITE}/configProperties.yml \${DEPLOY_DIR_NAME}/conf
            cp core-api/src/main/resources/resources-\${ENV}/\${SITE}/logback*.xml \${DEPLOY_DIR_NAME}/conf
            cp core-api/src/main/resources/application.yml \${DEPLOY_DIR_NAME}/conf
            cp -Rp core-api/src/main/resources/pem/v1/rsa-public.pem \${DEPLOY_DIR_NAME}/conf/pem.v1
            
            # cp deploy-conf/conf/\${ENV}/* \${DEPLOY_DIR_NAME}/conf
            # cp -R /usr/local/jdk-17.0.12_7 \${DEPLOY_DIR_NAME}
            
            echo "----------- Make zip file -----------"
            cd \${DEPLOY_DIR_NAME}
            zip -r \${DEPLOY_DIR_NAME} *
        """.stripIndent()
        ])
    }
}
