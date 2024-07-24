
def executeJob(String jobName, Map config) {
    echo "Executing job: ${jobName} for namespace: ${config.namespace}"
    // def job = load "${jobName}.groovy"
    "${jobName}"(config)
}

def autoApproval(String namespace, String jobName) {
    return { config ->
        stage("Auto Approval Deployment") {
            echo "Auto deploying to ${namespace}"
            // Trigger the provided jobName
            if (jobName) {
               // Call the referenced method directly
                executeJob(jobName, config + [namespace: namespace])
                // "${jobName}"(config + [namespace: namespace])
            } else {
                error "doDeploy function not provided."
            }
        }
    }
}

def manualApproval(String namespace, int timeoutMinutes = 60) {
    return { config ->
        stage("Manual Deploy ${namespace}") {
            def approved = false
            try {
                timeout(time: timeoutMinutes, unit: 'MINUTES') {
                    approved = input(
                        message: "Approve deployment to ${namespace}?",
                        ok: "Approve",
                        submitterParameter: 'APPROVER'
                    )
                }
            } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                // This exception is thrown when the timeout is exceeded or the input is aborted
                echo "Deployment to ${namespace} was not approved within ${timeoutMinutes} minutes or was aborted."
                return // Exit the closure without deploying
            }

            if (approved) {
                echo "Deployment to ${namespace} approved. Proceeding with deployment."
                if (config.doDeploy) {
                    "${config.doDeploy}"(config + [namespace: namespace])
                } else {
                    error "doDeploy function not provided."
                }
            } else {
                echo "Deployment to ${namespace} was not approved."
            }
        }
    }
}
