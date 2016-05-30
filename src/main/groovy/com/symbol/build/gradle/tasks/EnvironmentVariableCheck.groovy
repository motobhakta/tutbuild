package com.symbol.build.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * This will check required Environment variable has been set or not.
 */
class EnvironmentVariableCheck extends DefaultTask {

    @Input
    def envMap

    @TaskAction
    def checkEnvironmentVariable() {
        envMap.each { key, val ->
            if(System.getenv("${key}") == null){
                logger.error("ERROR: ${key} : ${val}")
            }
        }
    }

}
