import org.groot.jenkinslib.utils.PodTemplates

def call(Map args = [:], Closure body) {
  def label = args.label ?: "multi-tool-${UUID.randomUUID().toString()}"

  def podTemplates = new PodTemplates() // Create an instance of PodTemplates

  podTemplates.jnlpTemplate {
    podTemplates.dockerTemplate {
      podTemplates.awsCliTemplate {
        node(POD_LABEL) {
          sh "echo hello from $POD_LABEL"
          sh "echo hello from $label"
          body()
        }
      }
    }
  }
}