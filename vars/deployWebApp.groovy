def doDeploy(Map config) {
    stage("Deploying to ${config.namespace}") {
        lock("deploy-${config.namespace}-${config.applicationName}") {
            echo "Locked build pipeline for deployment of ${config.applicationName}"

        }
    }
}