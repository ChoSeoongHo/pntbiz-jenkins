package jobs.templates.maven

return { mavenTemplate ->
    return { Map baseConfig ->
        mavenTemplate.delegate = delegate
        mavenTemplate.resolveStrategy = DELEGATE_FIRST

        mavenTemplate(baseConfig + [
                repoUrl          : 'git@github.com:pntbiz1/pntbiz-server.git',
                credentialsId    : 'ssh-pntbiz-server',
                pomPath          : './pntbiz-root/pom.xml',
                mavenGoals       : 'clean install -Dmaven.test.skip=true -Denvironment=${ENV} -Dsite=${SITE}',
                withSkipTestParam: false
        ])
    }
}