return { Map config ->
    job(config.jobName) {
        description(config.description)

        logRotator {
            numToKeep(5)
        }

        steps {
            configure { project ->
                def builders = project / 'builders'

                builders << 'hudson.tasks.Shell' {
                    command("""
                        ncloud vserver ${config.action}ServerInstances --serverInstanceNoList ${config.instanceNo}
                    """.stripIndent())
                }

            }
        }
    }
}