package com.tutbuild.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction


class CreateDirStructureTask extends DefaultTask {

    @Input
    def dirMap

    @TaskAction
    def createFolderStructure(){
        envMap.each { key, val ->
            if(System.getenv("${key}") == null){
                logger.error("ERROR: ${key} : ${val}")
            }
        }
    }
}
