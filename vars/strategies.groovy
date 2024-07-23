def in_parallel(List tasks) {
    return { config ->
        def tasksMap = [:]
        tasks.each { task ->
            tasksMap["task-${tasks.indexOf(task)}"] = { task(config) }
        }
        parallel(tasksMap)
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
