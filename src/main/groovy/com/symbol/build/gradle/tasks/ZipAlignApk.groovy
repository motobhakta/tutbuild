package com.symbol.build.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class ZipAlignApk extends DefaultTask {
    @InputFile
    File inputFile

    @InputFile
    File zipAlignExe

    @OutputFile
    File outputFile

    @TaskAction
    void zipAlignTest() {
        println "${getZipAlignExe()} ${getInputFile()} ${getOutputFile()}"
        project.exec {
            executable = getZipAlignExe()
            args '-f'
            args '-v'
            args '4'
            args getInputFile()
            args getOutputFile()
        }
    }
}
