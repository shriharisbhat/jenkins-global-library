def call(Map config) {
    def strategyClosure = config.strategy ?: { throw new MissingMethodException("Required strategy like parallel or sequence not defined", this.class, []) }
    
    // Retrieve deployment logic
    // def doDeploy = config.doDeploy
    // if (!doDeploy) {
    //     throw new MissingMethodException("doDeploy not defined", this.class, [])
    // }
    
    // Execute each task like in_parallel() or sequence() defined in the strategy, checkout deploymentStrategy.groovy
    strategyClosure().each { task ->
        println "tasks $task(config)"
        task(config)
    }
}
