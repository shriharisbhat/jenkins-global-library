def call(Map args = [:], Closure body) {
    def label = args.label ?: "multi-tool-${UUID.randomUUID().toString()}"

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
  - name: aws-cli
    image: amazon/aws-cli:2.4.7
    command:
      - cat
    tty: true
  - name: kubectl
    image: bitnami/kubectl:latest
    command:
      - cat
    tty: true
    securityContext:
      runAsUser: 1000
      runAsGroup: 1000
  volumes:
    - name: dind-storage
      emptyDir: {}
"""
    ) {
        node(label) {
            body()
        }
    }
}