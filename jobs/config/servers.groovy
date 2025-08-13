package jobs.config

def ncloud = evaluate(readFileFromWorkspace('jobs/config/servers/ncloud.groovy'))
def nhncloud = evaluate(readFileFromWorkspace('jobs/config/servers/nhncloud.groovy'))

def mergeMaps(Map... maps) {
    def out = [:]
    maps.each { m ->
        m.each { k, v ->
            if (out.containsKey(k)) {
                throw new IllegalStateException("Duplicate key ${k}")
            }
            out[k] = v
        }
    }
    out
}

return mergeMaps(ncloud, nhncloud)
