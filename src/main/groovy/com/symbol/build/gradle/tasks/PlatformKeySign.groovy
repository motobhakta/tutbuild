package com.symbol.build.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class PlatformKeySign extends DefaultTask {

    @InputFile
    File certificateFile

    @InputFile
    File privateKeyFile

    @InputFile
    File inputFile

    @InputFile
    File signApkJar

    @OutputFile
    File outputFile


    @TaskAction
    void platformKeySigner() {
        project.javaexec {
            main="-jar"
            args = [
                    "${getSignApkJar()}",
                    "${getCertificateFile()}",
                    "${getPrivateKeyFile()}",
                    "${getInputFile()}",
                    "${getOutputFile()}"
            ]
        }
    }
}