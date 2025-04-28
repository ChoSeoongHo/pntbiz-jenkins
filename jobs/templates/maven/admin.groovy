package templates.maven

return { mavenJobTemplate ->
    return { Map baseConfig ->
        mavenJobTemplate.delegate = delegate
        mavenJobTemplate.resolveStrategy = DELEGATE_FIRST

        mavenJobTemplate(baseConfig + [
                repoUrl          : 'git@github.com:pntbiz1/pntbiz-indoorplus-admin.git',
                credentialsId    : 'ssh-pntbiz-indoorplus-admin',
                pomPath          : './pntbiz-admin2/pom.xml',
                mavenGoals       : 'clean install -Dmaven.test.skip=true -Denvironment=${ENV} -Dsite=${SITE}',
                withSkipTestParam: false
        ])
    }
}