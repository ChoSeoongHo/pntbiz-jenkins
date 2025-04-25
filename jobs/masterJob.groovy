def modules = evaluate(readFileFromWorkspace('dsl/config/modules.groovy'))
def servers = evaluate(readFileFromWorkspace('dsl/config/servers.groovy'))
def serverMatrix = evaluate(readFileFromWorkspace('dsl/config/serverMatrix.groovy'))

def apiTemplate = evaluate(readFileFromWorkSpace('dsl/templates/api.groovy'))

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
        def artifactName = moduleName.replace('-', '_') + '-0.0.1-SNAPSHOT.war'
        def jobName = "${server.suffix}-${moduleName}"
        def desc = "${server.description} ${moduleName.toUpperCase()} \nIP : ${server.ip}"

        println "[INFO] Creating job '${jobName}' for module '${moduleName}' on server '${serverKey}'..."
        apiTemplate.createApiJob([
                jobName    : jobName,
                description: desc,
                playbook   : server.playbook,
                inventory  : server.inventory,
                extraVars  : [
                        host          : 'local',
                        src_file_path : "/var/lib/jenkins/workspace/${jobName}/${modulePath}/target/${artifactName}",
                        desc_file_path: "/data/${jobName}/api.zip",
                        remote_host   : 'server-api',
                        archive_temp  : "/data/${jobName}/temp/",
                        sh_command    : "/data/naver/shell/deploy_api.sh"
                ],
                cleanup    : [
                        workspace   : "/var/lib/jenkins/workspace/${jobName}",
                        deployTarget: "/data/${jobName}"
                ]
        ])
        println "[SUCCESS] Job '${jobName}' created successfully."
    }
}
