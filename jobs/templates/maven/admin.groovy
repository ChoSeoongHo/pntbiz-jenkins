package templates.maven

return { mavenTemplate ->
    return { Map baseConfig ->
        mavenTemplate.delegate = delegate
        mavenTemplate.resolveStrategy = DELEGATE_FIRST

        mavenTemplate(baseConfig + [
                repoUrl          : 'git@github.com:pntbiz1/pntbiz-indoorplus-admin.git',
                credentialsId    : 'ssh-pntbiz-indoorplus-admin',
                pomPath          : './pntbiz-admin2/pom.xml',
                mavenGoals       : 'clean install -Dmaven.test.skip=true -Denvironment=${ENV} -Dsite=${SITE}',
                withSkipTestParam: false
        ])
    }
}