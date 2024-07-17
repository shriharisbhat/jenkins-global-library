pipelineJob('GenericTriggerJob') {
    description('Job triggered by generic webhook with docker version and environment parameters')

    parameters {
        stringParam('DOCKER_VERSION', '', 'Docker version to use')
        stringParam('ENVIRONMENT', '', 'Target environment')
    }

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/your-username/your-repo.git')
                        credentials('your-credentials-id')  // If needed
                    }
                    branch('main')  // Or whichever branch you want to use
                }
            }
            scriptPath('path/to/your/Jenkinsfile')  // Path to your Jenkinsfile in the repo
        }
    }

    triggers {
        genericTrigger {
            genericVariables {
                genericVariable {
                    key('DOCKER_VERSION')
                    value('$.docker_version')
                }
                genericVariable {
                    key('ENVIRONMENT')
                    value('$.environment')
                }
            }
            token('your-secret-token')  // Token to authenticate the webhook
            causeString('Triggered by generic webhook')
            regexpFilterText('$DOCKER_VERSION $ENVIRONMENT')
            regexpFilterExpression('^.+$.+$')  // This regex ensures both variables are non-empty
        }
    }
}