package jobs.templates.gradle

return { gradleTemplate ->
    return { Map baseConfig ->
        gradleTemplate.delegate = delegate
        gradleTemplate.resolveStrategy = DELEGATE_FIRST

        gradleTemplate(baseConfig + [
                repoUrl        : 'git@github.com:pntbiz1/pntbiz-kdnp-engine.git',
                credentialsId  : 'pntbiz-kdnp-engine',
                gradleTasks    : 'clean build -x test',
                gradleName     : 'gradle-8.10.2',
                jdk            : 'JDK17',
                defaultBranch  : 'develop',
                packagingScript: """
                    ZIP_NAME='kdnp.zip'
                    DEPLOY_FILE_NAME='pntbiz-kdnp-engine.jar'
                    DEPLOY_DIR_NAME='kdnp'
                    
                    echo "----------- Make temp directory -----------"
                    mkdir -p \${DEPLOY_DIR_NAME}
                    mkdir -p \${DEPLOY_DIR_NAME}/pem/\${PEM_VERSION}
                    mkdir -p \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- cp file -----------"
                    cp kdnp/build/libs/*.jar \${DEPLOY_DIR_NAME}
                    cp deploy-conf/develop/sh/*.sh \${DEPLOY_DIR_NAME}
                    cp deploy-conf/develop/conf/* \${DEPLOY_DIR_NAME}/conf
                    
                    echo "----------- Make zip file -----------"
                    cd "\${DEPLOY_DIR_NAME}"
                    zip -r "../\${DEPLOY_DIR_NAME}.zip" *
        """.stripIndent()
        ])
    }
}
