def in_parallel(List tasks) {
    return { config ->
        steps.parallel tasks.collectEntries { task ->
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
        steps.stage("Auto Deploy ${namespace}") {
            steps.echo "Auto deploying to ${namespace}"
            deploySonicApp.doDeploy(config, namespace)
        }
    }
}

def approval(String namespace) {
    return { config ->
        steps.stage("Approval Deploy ${namespace}") {
            steps.input message: "Approve deployment to ${namespace}?", ok: "Approve"
            steps.echo "Deploying to ${namespace}"
            deploySonicApp.doDeploy(config, namespace)
        }
    }
}
