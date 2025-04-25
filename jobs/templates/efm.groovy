return { Map baseConfig ->
    def gradleJobTemplate = evaluate(readFileFromWorkspace('jobs/templates/gradleJob.groovy'))
    gradleJobTemplate.delegate = this
    gradleJobTemplate.resolveStrategy = Closure.DELEGATE_FIRST

    gradleJobTemplate(baseConfig + [
            repoUrl        : 'git@github.com:pntbiz1/pntbiz-raas-efm.git',
            credentialsId  : 'ssh-pntbiz-raas-efm',
            gradleTasks    : 'clean build -x test',
            gradleName     : 'gradle-8.10.2',
            jdk            : 'JDK17',
            packagingScript: """
            ZIP_NAME='efm.zip'
            DEPLOY_FILE_NAME='pntbiz-raas-efm.jar'
            DEPLOY_DIR_NAME='efm'

            mkdir -p \${DEPLOY_DIR_NAME}/conf
            cp pntbiz-raas-efm-api/build/libs/*.jar \${DEPLOY_DIR_NAME}
            cp deploy-conf/sh/dev/*.sh \${DEPLOY_DIR_NAME}
            cp deploy-conf/conf/dev/* \${DEPLOY_DIR_NAME}/conf
            cp -R /usr/local/jdk-17.0.12_7 \${DEPLOY_DIR_NAME}
            cd \${DEPLOY_DIR_NAME}
            zip -r \${DEPLOY_DIR_NAME} *
        """.stripIndent()
    ])
}
