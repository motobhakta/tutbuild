package com.symbol.build.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

class ValidateKeySigning extends DefaultTask {
    @InputFile
    File inputFile

    def getValidateCommand(){
        def os = System.getProperty("os.name").toLowerCase()

        def validateCommand
        if (os.startsWith("Windows")) {
            validateCommand = "jarsigner.exe";
        } else {
            validateCommand = "jarsigner";
        }

        def javaHome = System.getenv("JAVA_HOME")

        if (javaHome != null && javaHome.length() > 0) {
            validateCommand = "${javaHome}${File.separator}bin${File.separator}${validateCommand}"
        }

        validateCommand

    }

    @TaskAction
    void validatePlatformKeySigning(){
        println "${getValidateCommand()} -verify -certs -verbose ${getInputFile()}"
        project.exec {
            executable = getValidateCommand()
            args '-verify'
            args '-certs'
            args '-verbose'
            args getInputFile()
        }
    }
}