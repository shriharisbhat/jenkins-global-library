def call(Map config) {
    def strategy = config.strategy ?: { [] }
    
    strategy().each { task ->
        task(config)
    }
}

def doDeploy(Map config, String namespace) {
    stage("Deploying to ${namespace}") {
        lock("deploy-${namespace}-${config.applicationName}") {
            echo "Locked build pipeline for deployment of ${config.applicationName}"

        }
    }
}

def getEnvironmentConfig() {
    // Implementation to get environment config
}

def getClusterKubeConfigPath(cluster) {
    // Implementation to get cluster kubeconfig path
}

def checkoutInfraRepo() {
    // Implementation to checkout infra repo
}

def performDeploy(String clusterKubeConfig, envConfig) {
    // Implementation to perform deploy
}
