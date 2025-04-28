package templates.maven

return { mavenJobTemplate ->
    return { Map baseConfig ->
        mavenJobTemplate.delegate = delegate
        mavenJobTemplate.resolveStrategy = DELEGATE_FIRST

        mavenJobTemplate(baseConfig + [
                repoUrl          : 'git@github.com:pntbiz1/pntbiz-indoorplus-wms.git',
                credentialsId    : 'ssh-pntbiz-indoorplus-wms',
                pomPath          : './pntbiz-wms/pom.xml',
                mavenGoals       : 'clean install -Dmaven.test.skip=true -Denvironment=${ENV} -Dsite=${SITE}',
                withSkipTestParam: false
        ])
    }
}