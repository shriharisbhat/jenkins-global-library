// def in_parallel(List tasks) {
//     return { config ->
//         def tasksMap = [:]
//         tasks.each { task ->
//             tasksMap["task-${tasks.indexOf(task)}"] = { task(config) }
//         }
//         parallel(tasksMap)
//     }
// }

def in_parallel(List tasks) {
    return { config ->
        parallel(tasks.collectEntries { task ->
            ["task-${tasks.indexOf(task)}": { task(config) }]
        })
    }
}

def sequence(List tasks) {
    return { config ->
        tasks.each { task ->
            task(config)
        }
    }
}