def modules = evaluate(readFileFromWorkspace('jobs/config/modules.groovy'))
def servers = evaluate(readFileFromWorkspace('jobs/config/servers.groovy'))
def serverMatrix = evaluate(readFileFromWorkspace('jobs/config/serverMatrix.groovy'))
def mavenTemplate = evaluate(readFileFromWorkspace('jobs/templates/maven/mavenJob.groovy'))
def gradleTemplate = evaluate(readFileFromWorkspace('jobs/templates/gradle/gradleJob.groovy'))

def instanceManageJobGenerator = evaluate(readFileFromWorkspace('jobs/templates/bash/instanceManageJob.groovy'))
def jobGenerators = [
        api                  : evaluate(readFileFromWorkspace('jobs/templates/maven/api.groovy'))(mavenTemplate),
        wms                  : evaluate(readFileFromWorkspace('jobs/templates/maven/wms.groovy'))(mavenTemplate),
        admin                : evaluate(readFileFromWorkspace('jobs/templates/maven/admin.groovy'))(mavenTemplate),
        rtls                 : evaluate(readFileFromWorkspace('jobs/templates/nodejs/rtls.groovy')),
        socket               : evaluate(readFileFromWorkspace('jobs/templates/nodejs/socket.groovy')),
        oauth                : evaluate(readFileFromWorkspace('jobs/templates/gradle/oauth.groovy'))(gradleTemplate),
        smart_sensing_core   : evaluate(readFileFromWorkspace('jobs/templates/gradle/smartSensingCore.groovy'))(gradleTemplate),
        smart_sensing_service: evaluate(readFileFromWorkspace('jobs/templates/gradle/smartSensingService.groovy'))(gradleTemplate),
        efm                  : evaluate(readFileFromWorkspace('jobs/templates/gradle/efm.groovy'))(gradleTemplate)
]

//println("[INFO] Start generating instance-management jobs...")
//def totalInstanceJobs = 0
//instanceManageJobGenerator.delegate = this
//instanceManageJobGenerator.resolveStrategy = Closure.DELEGATE_FIRST
//servers.each { serverKey, serverInfo ->
//    def server = servers[serverKey]
//    ['start', 'stop'].each { action ->
//        def jobName = "${action}-${serverKey}"
//        println("[INFO] Start syncing instance job '${jobName}'...")
//        instanceManageJobGenerator(
//                jobName: jobName,
//                description: "${action.capitalize()} ${server.description} Job\n(IP: ${server.ip})",
//                instanceNo: server.instanceNo,
//                action: action
//        )
//        println("[INFO] Instance job '${jobName}' synced successfully.")
//        totalInstanceJobs++
//    }
//}
//println "[INFO] Instance-management jobs sync completed. Total: ${totalInstanceJobs} jobs."

println("[INFO] Start generating deploy jobs...")
def totalDeployJobs = 0
serverMatrix.each { serverKey, modulesForServer ->
    def server = servers[serverKey]
    println "[INFO] Processing server '${serverKey}' (${server.description}/${server.ip})"

    modulesForServer.each { moduleName ->
        def modulePath = modules[moduleName]
        if (!modulePath) {
            println "[WARN] Module '${moduleName}' is not defined in 'modules' map. Skipping."
            return
        }

        def jobGenerator = jobGenerators[moduleName]
        if (!jobGenerator) {
            println "[WARN] No job generator defined for module '${moduleName}'. Skipping."
            return
        }

        def artifactName = 'pntbiz_' + moduleName.replace('-', '_') + '-0.0.1-SNAPSHOT.war'
        def jobName = "${server.suffix}-${moduleName}"
        def desc = "${server.description} ${moduleName.toUpperCase()} \nIP : ${server.ip}"

        println "[INFO] Start syncing deploy job '${jobName}'..."

        jobGenerator.delegate = this
        jobGenerator.resolveStrategy = DELEGATE_FIRST
        jobGenerator([
                serverKey    : serverKey,
                instanceNo   : server.instanceNo,
                jobName      : jobName,
                description  : desc,
                playbook     : server.playbook,
                inventory    : server.inventory,
                defaultBranch: server.defaultBranch ?: 'develop',
                env          : server.env ?: 'develop',
                site         : server.site ?: 'common',
                extraVars    : [
                        host          : 'local',
                        src_file_path : "/var/lib/jenkins/workspace/${jobName}/${modulePath}/target/${artifactName}",
                        desc_file_path: "/data/${jobName}/${moduleName}.zip",
                        remote_host   : "server-${moduleName}",
                        archive_temp  : "/data/${jobName}/temp/",
                        sh_command    : "/data/naver/shell/deploy_${moduleName}.sh"
                ],
                cleanup      : [
                        workspace   : "/var/lib/jenkins/workspace/${jobName}",
                        deployTarget: "/data/${jobName}"
                ]
        ])

        println "[INFO] Deploy job '${jobName}' synced successfully."
        totalDeployJobs++
    }
}
println "[INFO] Deploy jobs sync completed. Total: ${totalDeployJobs} jobs."

//def totalJobs = totalInstanceJobs + totalDeployJobs
//println "[INFO] Job sync process finished. Total jobs synced: ${totalJobs}"