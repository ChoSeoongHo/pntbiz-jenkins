package jobs.templates.bash

return { Map config ->
    job(config.jobName) {
        description(config.description)

        logRotator {
            numToKeep(5)
        }

        steps {
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
