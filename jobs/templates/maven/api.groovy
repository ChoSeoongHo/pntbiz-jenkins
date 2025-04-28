package templates.maven

return { mavenTemplate ->
    return { Map baseConfig ->
        mavenTemplate.delegate = delegate
        mavenTemplate.resolveStrategy = DELEGATE_FIRST

        mavenTemplate(baseConfig + [
                repoUrl          : 'git@github.com:pntbiz1/pntbiz-indoorplus-api.git',
                credentialsId    : 'ssh-pntbiz-indoorplus-api',
                pomPath          : './pntbiz-api/pom.xml',
                mavenGoals       : 'clean install -Dmaven.test.skip=${SKIP_TEST} -Denvironment=${ENV} -Dsite=${SITE}',
                withSkipTestParam: true
        ])
    }
}