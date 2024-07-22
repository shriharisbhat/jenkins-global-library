import org.groot.jenkinslib.utils.PodTemplates

def call(Map args = [:], Closure body) {
  def label = args.label ?: "multi-tool-${UUID.randomUUID().toString()}"
  def cloudName = args.cloud ?: 'kubernetes'

  def podTemplates = new PodTemplates() // Create an instance of PodTemplates

  podTemplates.dockerTemplate(cloudName: cloudName) {
    podTemplates.awsCliTemplate {
      node(POD_LABEL) {
        sh "echo hello from $POD_LABE"
        body()
      }
    }
  }
}