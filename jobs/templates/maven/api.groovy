return { Map config ->
    mavenJobTemplate.delegate = delegate
    mavenJobTemplate.resolveStrategy = Closure.DELEGATE_FIRST

    mavenJobTemplate(config + [
            repoUrl      : 'git@github.com:pntbiz1/pntbiz-indoorplus-api.git',
            credentialsId: 'ssh-pntbiz-indoorplus-api',
            pomPath      : './pntbiz-api/pom.xml',
            mavenGoals   : 'clean install -Dmaven.test.skip=${SKIP_TEST} -Denvironment=${ENV} -Dsite=${SITE}',
            withSkipTestParam: true
    ])
}