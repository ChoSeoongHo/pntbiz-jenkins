return { Map config ->
    mavenJobTemplate.delegate = delegate
    mavenJobTemplate.resolveStrategy = Closure.DELEGATE_FIRST

    mavenJobTemplate(config + [
            repoUrl      : 'git@github.com:pntbiz1/pntbiz-indoorplus-wms.git',
            credentialsId: 'ssh-pntbiz-indoorplus-wms',
            pomPath      : './pntbiz-wms/pom.xml',
            mavenGoals   : 'clean install -Dmaven.test.skip=true -Denvironment=${ENV} -Dsite=${SITE}',
            withSkipTestParam: false
    ])
}