def call(Map config) {
      stage("Deploying to ${config.namespace}") {
         lock("deploy-${config.namespace}-${config.applicationName}") {
            echo "Locked build pipeline for deployment of ${config.applicationName}"
            multiToolPodTemplate(label: 'Deploy web app') {
               container('docker') {
                  sh 'docker version'
                  sh 'docker pull nginx:latest'
               }
               container('aws-cli') {
                  // withCredentials([file(credentialsId: 'aws-credentials', variable: 'AWS_SHARED_CREDENTIALS_FILE')]) {
                  sh 'aws --version'
                  // sh 'aws s3 ls'
                  // }
               }
               // container('kubectl') {
               //     // withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
               //          sh 'echo $HOSTNAME'
               //          sh 'env | sort'
               //          sh 'which kubectl || echo "kubectl not found"'
               //          sh '''
               //             set -x
               //             kubectl version --client
               //             env | sort
               //          '''

               //     // }
               }

            }
         }
      }