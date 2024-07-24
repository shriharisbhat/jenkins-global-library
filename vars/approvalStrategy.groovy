def autoApproval(String namespace) {
    return { config ->
        stage("Auto Deploy ${namespace}") {
            echo "autoApproval deploying to ${namespace}"
            // Trigger the provided doDeploy function
            if (config.doDeploy) {
                // Call the referenced method directly
                "${config.doDeploy}"(config + [namespace: namespace])
            } else {
                error "doDeploy function not provided."
            }
        }
    }
}

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
