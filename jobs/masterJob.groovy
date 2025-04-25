def modules = evaluate(readFileFromWorkspace('jobs/config/modules.groovy'))
def servers = evaluate(readFileFromWorkspace('jobs/config/servers.groovy'))
def serverMatrix = evaluate(readFileFromWorkspace('jobs/config/serverMatrix.groovy'))
def gradleJobTemplate = evaluate(readFileFromWorkspace('jobs/templates/gradleJob.groovy'))

def jobGenerators = [
        api   : evaluate(readFileFromWorkspace('jobs/templates/api.groovy')),
        wms   : evaluate(readFileFromWorkspace('jobs/templates/wms.groovy')),
        admin : evaluate(readFileFromWorkspace('jobs/templates/admin.groovy')),
        rtls  : evaluate(readFileFromWorkspace('jobs/templates/rtls.groovy')),
        socket: evaluate(readFileFromWorkspace('jobs/templates/socket.groovy')),
        efm   : evaluate(readFileFromWorkspace('jobs/templates/efm.groovy'))(gradleJobTemplate)
]

println("[INFO] Start generating deploy jobs...")
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
                cleanup: [
                        workspace   : "/var/lib/jenkins/workspace/${jobName}",
                        deployTarget: "/data/${jobName}"
                ]
        ])

        println "[SUCCESS] Job '${jobName}' created successfully."
    }
}
