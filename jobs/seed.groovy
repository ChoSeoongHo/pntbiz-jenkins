job('seed-job') {
    description('모든 Job DSL을 실행하여 Jenkins Job을 동기화')

    scm {
        git {
            remote {
                url('git@github.com:ChoSeoongHo/pntbiz-jenkins.git')
                credentials('ssh-pntbiz-jenkins')
            }
            branch('main')
        }
    }

    steps {
        dsl {
            external('jobs/masterJob.groovy')
            removeAction('DELETE')
        }
    }

    triggers {
        scm('H/5 * * * *')
    }
}
