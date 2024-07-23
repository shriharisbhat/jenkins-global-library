    static Map<String, Map<String, Object>> pipelineJobs() {
        def pipelineJobsList = [  
            'groot-web-app-job': [
                'yourUsername': 'shriharisbhat',
                'yourRepo': 'groot-client-app',
                'scriptPath': 'jenkins/Jenkinsfile.deploy',
                'branches': '*/main',
                'triggerOnGitPush': false,
                'triggerGenericWebhook': true,
                'useDockerTagParameter' : true,
                'useDeployEnvironments' : true,
                'githubOrg': '',
                'checkoutCredentialsId': '',
            ],
            'storm-web-app-job': [
                'yourUsername': 'shriharisbhat',
                'yourRepo': 'storm-client-app',
                'scriptPath': 'jenkins/Jenkinsfile.deploy',
                'branches': '*/main',
                'triggerOnGitPush': false,
                'triggerGenericWebhook': true,
                'useDockerTagParameter' : true,
                'useDeployEnvironments' : true,
                'githubOrg': '',
                'checkoutCredentialsId': '',
            ],
        ]

        return pipelineJobsList as Map<String, Map<String, Object>>
    }
