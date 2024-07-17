import static pipelineConfig.pipeConfig

def createPipelineJob(String jobName, Map < String, Object > values) {
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
                            url("https://github.com/$values.yourUsername/$values.yourRepo.git'")
                            credentials('your-credentials-id') // If needed
                        }
                        branch(values.branches) // Or whichever branch you want to use
                    }
                }
                scriptPath(values.scriptPath) // Path to your Jenkinsfile in the repo
            }
        }

        logRotator {
            if (soxExtendLogRotator) {
                // for SOX compliance, keep builds for 15 months
                daysToKeep(465)
                artifactDaysToKeep(465)
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

                    if (triggerTokenSecretId) {
                        tokenCredentialId(triggerTokenSecretId)
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

appPipeConfig.forEach { String jobName, Map < String, Object > values ->
        createPipelineJob(jobName, values)
}