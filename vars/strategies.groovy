def in_parallel(List tasks) {
    return { config ->
        parallel tasks.collectEntries { task ->
            ["${task.name}": { task(config) }]
        }
    }
}

def sequence(List tasks) {
    return { config ->
        tasks.each { task ->
            task(config)
        }
    }
}

def auto(String namespace) {
    return { config ->
        stage("Auto Deploy ${namespace}") {
            echo "Auto deploying to ${namespace}"
            deploySonicApp.doDeploy(config, namespace)
        }
    }
}

def approval(String namespace) {
    return { config ->
        stage("Approval Deploy ${namespace}") {
            input message: "Approve deployment to ${namespace}?", ok: "Approve"
            echo "Deploying to ${namespace}"
            deploySonicApp.doDeploy(config, namespace)
        }
    }
}
