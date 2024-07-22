import org.groot.jenkinslib.utils.PodTemplates

def call(Map args = [:], Closure body) {
  def label = args.label ?: "multi-tool-${UUID.randomUUID().toString()}"
  def cloudName = args.cloud ?: 'kubernetes'

  def podTemplates = new PodTemplates() // Create an instance of PodTemplates

  podTemplates.dockerTemplate(cloud: cloudName) {
    podTemplates.awsCliTemplate(cloud: cloudName) {
      node(POD_LABEL) {
        sh "echo hello from $POD_LABEL"
        body()
      }
    }
  }
}