import static pipelineConfig.pipelineJobs

def createPipelineJob(String jobName, Map<String, Object> values) {
    pipelineJob(jobName) {
        description("Pipeline for $jobName")

        if (values.useDockerTagParameter) {
            parameters {
                stringParam('DOCKER_VERSION', '', 'Docker version to use')
            }
        }

        if (values.useDeployEnvironments) {
            parameters {
                stringParam('ENVIRONMENT', '', 'Target environment')
            }
        }

        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url("https://github.com/${values.yourUsername}/${values.yourRepo}.git")
                            credentials(values.checkoutCredentialsId) // If needed
                        }
                        branch(values.branches) // Or whichever branch you want to use
                    }
                }
                scriptPath(values.scriptPath) // Path to your Jenkinsfile in the repo
            }
        }

        logRotator {
            if (values.soxExtendLogRotator) {
                daysToKeep(365)
                artifactDaysToKeep(365)
            } else {
                numToKeep(100)
                artifactNumToKeep(100)
            }
        }

        if (values.triggerGenericWebhook) {
            triggers {
                genericTrigger {
                    genericVariables {
                        if (values.useDockerTagParameter) {
                            genericVariable {
                                key('DOCKER_VERSION')
                                value("\$.docker_version")
                            }
                        }
                        if (values.useDeployEnvironments) {
                            genericVariable {
                                key('ENVIRONMENT')
                                value("\$.environment")
                            }
                        }
                    }

                    if (values.triggerTokenSecretId) {
                        tokenCredentialId(values.triggerTokenSecretId)
                    } else {
                        token(values.yourRepo) // Token to authenticate the webhook
                    }

                    causeString('Triggered by generic webhook')
                    regexpFilterText('$DOCKER_VERSION $ENVIRONMENT')
                    regexpFilterExpression('^.+$.+$') // This regex ensures both variables are non-empty
                }
            }
        }
    }
}

// Ensure pipelineJobs is properly called and loop over it
pipelineJobs().forEach { String jobName, Map<String, Object> values ->
    createPipelineJob(jobName, values)
}
