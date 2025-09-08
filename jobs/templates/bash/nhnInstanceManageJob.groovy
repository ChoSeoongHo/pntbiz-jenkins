package jobs.templates.bash

return { Map config ->
    job(config.jobName) {
        description(config.description)

        logRotator {
            numToKeep(5)
        }

        if (config.action == 'start' && config.startAt) {
            triggers {
                cron(config.startAt)
            }
        }

        if (config.action == 'stop' && config.stopAt) {
            triggers {
                cron(config.stopAt)
            }
        }

        steps {
            configure { project ->
                def builders = project / 'builders'

                builders << 'org.jenkinsci.plugins.ansible.AnsiblePlaybookBuilder' {
                    playbook("/etc/ansible/nhncloud/${config.projectType}/instance_control.yml")
                    ansibleName('ANSIBLE_HOME')
                    forks(5)
                    unbufferedOutput(true)

                    extraVars {
                        'org.jenkinsci.plugins.ansible.ExtraVar' {
                            key('server_id_param')
                            value(config.instanceNo)
                            hidden(true)
                        }

                        'org.jenkinsci.plugins.ansible.ExtraVar' {
                            key('instance_action_param')
                            value(config.action)
                            hidden(false)
                        }
                    }
                }
            }
        }
    }
}
