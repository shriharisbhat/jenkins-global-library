def autoApproval(String namespace) {
    return { config ->
        stage("Auto Approval Deployment") {
            echo "Auto deploying to ${namespace}"
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
        stage("Manual Approval Deployment") {
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
