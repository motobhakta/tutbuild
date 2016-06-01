package com.tutbuild.tasks

import org.ajoberstar.grgit.Grgit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path
import java.nio.file.Paths

import java.nio.file.Files

class GitCheckoutTask extends DefaultTask {

    @Input
    def cloneLocation

    @Input
    def commitId

    @Input
    def repoLocation

    @TaskAction
    def getCode(){
        def grgit

        Path dir = Paths.get(getCloneLocation())
        if(Files.exists(dir)){
            grgit = Grgit.open(dir: "${getCloneLocation()}")
        }else{
            grgit = Grgit.clone(dir: "${getCloneLocation()}", uri: "${getRepoLocation()}")
        }
        grgit.checkout(branch: "${getCommitId()}")
    }

}
