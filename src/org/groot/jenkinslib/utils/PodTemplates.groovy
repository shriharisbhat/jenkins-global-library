pipeline {
  agent any
  stages {
    stage('Unit Test') {
      steps {
        sh 'mvn clean test'
      }
    }
    stage('Deploy Standalone') {
      steps {
        sh 'mvn deploy -P standalone'
      }
    }
    stage('Deploy AnyPoint') {
      environment {
        ANYPOINT_CREDENTIALS = credentials('anypoint.credentials')
      }
      steps {
        sh 'mvn deploy -P arm -Darm.target.name=local-4.0.0-ee -Danypoint.username=${ANYPOINT_CREDENTIALS_USR}  -Danypoint.password=${ANYPOINT_CREDENTIALS_PSW}'
      }
    }
    stage('Deploy CloudHub') {
      environment {
        ANYPOINT_CREDENTIALS = credentials('anypoint.credentials')
      }
      steps {
        sh 'mvn deploy -P cloudhub -Dmule.version=4.0.0 -Danypoint.username=${ANYPOINT_CREDENTIALS_USR} -Danypoint.password=${ANYPOINT_CREDENTIALS_PSW}'
      }
    }
  }
}
package org.groot.jenkinslib.utils

public void jnlpTemplate(Map args = [: ], Closure body) {
  podTemplate(label: 'jnlp',
    serviceAccount: 'jenkins-k8s-jenkins',
    containers: [
      containerTemplate(
        name: 'jnlp',
        image: 'jenkins/inbound-agent:3256.v88a_f6e922152-1',
        args: '${computer.jnlpmac} ${computer.name}',
        workingDir: '/home/jenkins/agent',
        envVars: [],
        resourceRequestCpu: '200m',
        resourceLimitCpu: '1',
        resourceRequestMemory: '256Mi',
        resourceLimitMemory: '1Gi'
      )
    ]
  ) {
    body.call()
  }
}

public void dockerTemplate(Map args = [: ], Closure body) {
  podTemplate(
    containers: [
      containerTemplate(
        name: 'docker',
        image: 'docker:20.10.14-dind',
        privileged: true,
        ttyEnabled: true,
        envVars: [
          envVar(key: 'DOCKER_TLS_CERTDIR', value: '')
        ],
        volumeMounts: [
          emptyDirVolume(mountPath: '/var/lib/docker', name: 'dind-storage')
        ],
      )
    ],
    volumes: [
      emptyDirVolume(mountPath: '/var/lib/docker', memory: false, name: 'dind-storage')
    ]
  ) {
    body.call()
  }
}

public void awsCliTemplate(Map args = [: ], Closure body) {
  def defaultArgs = [
    name: 'aws-cli',
    image: 'amazon/aws-cli:2.4.7',
    command: 'cat',
    ttyEnabled: true
  ]

  def containerArgs = defaultArgs + args

  podTemplate(
    containers: [
      containerTemplate(
        containerArgs
      )
    ]
  ) {
    body.call()
  }
}

public void kubectlTemplate(Map args = [: ], Closure body) {
  def defaultArgs = [
    name: 'kubectl',
    image: 'bitnami/kubectl:latest',
    command: 'cat',
    ttyEnabled: true,
    runAsUser: '1000',
    runAsGroup: '1000',
  ]

  def containerArgs = defaultArgs + args

  podTemplate(
    containers: [
      containerTemplate(
        containerArgs
      )
    ]
  ) {
    body.call()
  }
}

public void mavenTemplate(body) {
  podTemplate(
    containers: [containerTemplate(name: 'maven', image: 'maven', command: 'sleep', args: '99d')],
    volumes: [secretVolume(secretName: 'maven-settings', mountPath: '/root/.m2'),
      persistentVolumeClaim(claimName: 'maven-local-repo', mountPath: '/root/.m2repo')
    ]) {
    body.call()
  }
}

return this