def modules = evaluate(readFileFromWorkspace('jobs/config/modules.groovy'))
def servers = evaluate(readFileFromWorkspace('jobs/config/servers.groovy'))
def serverMatrix = evaluate(readFileFromWorkspace('jobs/config/serverMatrix.groovy'))
def mavenJobTemplate = evaluate(readFileFromWorkspace('jobs/templates/maven/mavenJob.groovy'))
def gradleJobTemplate = evaluate(readFileFromWorkspace('jobs/templates/gradle/gradleJob.groovy'))

def instanceManageJobGenerator = evaluate(readFileFromWorkspace('jobs/templates/bash/instanceManageJob.groovy'))
def jobGenerators = [
        api                  : evaluate(readFileFromWorkspace('jobs/templates/maven/api.groovy'))(mavenJobTemplate),
        wms                  : evaluate(readFileFromWorkspace('jobs/templates/maven/wms.groovy'))(mavenJobTemplate),
        admin                : evaluate(readFileFromWorkspace('jobs/templates/maven/admin.groovy'))(mavenJobTemplate),
        rtls                 : evaluate(readFileFromWorkspace('jobs/templates/nodejs/rtls.groovy')),
        socket               : evaluate(readFileFromWorkspace('jobs/templates/nodejs/socket.groovy')),
        oauth                : evaluate(readFileFromWorkspace('jobs/templates/gradle/oauth.groovy'))(gradleJobTemplate),
        smart_sensing_core   : evaluate(readFileFromWorkspace('jobs/templates/gradle/smartSensingCore.groovy'))(gradleJobTemplate),
        smart_sensing_service: evaluate(readFileFromWorkspace('jobs/templates/gradle/smartSensingService.groovy'))(gradleJobTemplate),
        efm                  : evaluate(readFileFromWorkspace('jobs/templates/gradle/efm.groovy'))(gradleJobTemplate)
]

println("[INFO] Start generating jenkins jobs...")
instanceManageJobGenerator.delegate = this
instanceManageJobGenerator.resolveStrategy = Closure.DELEGATE_FIRST
serverMatrix.each { serverKey, modulesForServer ->
    def server = servers[serverKey]
    println "[INFO] Processing server '${serverKey}' (${server.description}/${server.ip})"

    ['start', 'stop'].each { action ->
        def jobName = "${action}-${serverKey}"
        instanceManageJobGenerator(
                jobName: jobName,
                description: "Server ${action.capitalize()} Job for ${server.description} (${server.ip})",
                instanceNo: server.instanceNo,
                action: action
        )
    }

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

        def artifactName = moduleName.replace('-', '_') + '-0.0.1-SNAPSHOT.war'
        def jobName = "${server.suffix}-${moduleName}"
        def desc = "${server.description} ${moduleName.toUpperCase()} \nIP : ${server.ip}"

        println "[INFO] Creating job '${jobName}' for module '${moduleName}' on server '${serverKey}'..."

        jobGenerator.delegate = this
        jobGenerator.resolveStrategy = Closure.DELEGATE_FIRST
        jobGenerator([
                serverKey  : serverKey,
                instanceNo : server.instanceNo,
                jobName    : jobName,
                description: desc,
                playbook   : server.playbook,
                inventory  : server.inventory,
                extraVars  : [
                        host          : 'local',
                        src_file_path : "/var/lib/jenkins/workspace/${jobName}/${modulePath}/target/${artifactName}",
                        desc_file_path: "/data/${jobName}/${moduleName}.zip",
                        remote_host   : "server-${moduleName}",
                        archive_temp  : "/data/${jobName}/temp/",
                        sh_command    : "/data/naver/shell/deploy_${moduleName}.sh"
                ],
                cleanup    : [
                        workspace   : "/var/lib/jenkins/workspace/${jobName}",
                        deployTarget: "/data/${jobName}"
                ]
        ])

        println "[SUCCESS] Job '${jobName}' created successfully."
    }
}