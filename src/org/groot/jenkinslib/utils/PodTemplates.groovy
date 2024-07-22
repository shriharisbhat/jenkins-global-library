package org.groot.jenkinslib.utils

public void jnlpTemplate(Map args = [:], Closure body) {
  def defaultArgs = [
     name: 'jnlp',
        image: 'jenkins/inbound-agent:3256.v88a_f6e922152-1',
        args: '${computer.jnlpmac} ${computer.name}',
        workingDir: '/home/jenkins/agent',
        resourceRequestCpu: '200m',
        resourceLimitCpu: '1',
        resourceRequestMemory: '256Mi',
        resourceLimitMemory: '512Mi'
  ]

  def containerArgs = defaultArgs + args

  podTemplate(
    containers: [
      containerTemplate(containerArgs)
    ]
  ) {
    body.call()
  }
}

public void dockerTemplate(Map args = [:], Closure body) {
   podTemplate(
        yaml: """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: docker
    image: docker:20.10.14-dind
    securityContext:
      privileged: true
    env:
      - name: DOCKER_TLS_CERTDIR
        value: ""
    volumeMounts:
      - name: dind-storage
        mountPath: /var/lib/docker
  volumes:
    - name: dind-storage
      emptyDir: {}
"""
  ) {
    body.call()
  }
}

public void awsCliTemplate(Map args = [:], Closure body) {
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

public void kubectlTemplate(Map args = [:], Closure body) {
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