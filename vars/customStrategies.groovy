
@NonCPS
def in_parallel(List tasks) {
    return { config ->
        def tasksMap = [:]
        tasks.each { task ->
            tasksMap["task-${tasks.indexOf(task)}"] = { task(config) }
        }
        parallel(tasksMap)
    }
}

@NonCPS
def sequence(List tasks) {
    return { config ->
        tasks.each { task ->
            task(config)
        }
    }
}

@NonCPS
def autoApproval(String namespace) {
    return { config ->
        stage("Auto Deploy ${namespace}") {
            echo "autoApproval deploying to ${namespace}"
            // Trigger the provided doDeploy function
            if (config.doDeploy) {
                config.doDeploy(config + [namespace: namespace])
            } else {
                error "doDeploy function not provided."
            }
        }
    }
}

@NonCPS
def manualApproval(String namespace) {
    return { config ->
        stage("Manual Deploy ${namespace}") {
            input message: "Approve deployment to ${namespace}?", ok: "Approve"
            echo "Deploying to ${namespace}"
            // Trigger the provided doDeploy function
            if (config.doDeploy) {
                config.doDeploy(config + [namespace: namespace])
            } else {
                error "doDeploy function not provided."
            }
        }
    }
}
