def call(Map config) {
    stage("Deploying node to ${config.namespace}") {
        lock("deploy-${config.namespace}-${config.applicationName}") {
            echo "Locked build pipeline for deployment of ${config.applicationName}"
            node(POD_LABE) {
                sh "echo deploying ${config.applicationName} to ${config.namespace}"
            }

        }
    }
}