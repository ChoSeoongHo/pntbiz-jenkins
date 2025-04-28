def servers = evaluate(readFileFromWorkspace('jobs/config/servers.groovy'))
def serverMatrix = evaluate(readFileFromWorkspace('jobs/config/serverMatrix.groovy'))
def instanceManageJobTemplate = evaluate(readFileFromWorkspace('jobs/templates/bash/instanceManageJob.groovy'))

println("[INFO] Start generating instance management jobs...")
instanceManageJobTemplate.delegate = this
instanceManageJobTemplate.resolveStrategy = Closure.DELEGATE_FIRST
serverMatrix.each { serverKey, modulesForServer ->
    def server = servers[serverKey]
    println "[INFO] Processing server '${serverKey}' (${server.description}/${server.ip})"

    ['start', 'stop'].each { action ->
        def jobName = "${action}-${serverKey}"
        instanceManageJobTemplate(
                jobName: jobName,
                description: "Server ${action.capitalize()} Job for ${server.description} (${server.ip})",
                instanceNo: server.instanceNo,
                action: action
        )
    }
}