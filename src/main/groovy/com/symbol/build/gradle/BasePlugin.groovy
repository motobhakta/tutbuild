package com.symbol.build.gradle

import org.gradle.api.Project
import org.gradle.api.Task

import com.symbol.build.gradle.tasks.PlatformKeySign
import com.symbol.build.gradle.tasks.ValidateKeySigning
import com.symbol.build.gradle.tasks.ZipAlignApk
import com.symbol.build.gradle.tasks.EnvironmentVariableCheck

public abstract class BasePlugin {
    public static final String SYMBOL_GROUP = "Zebra Tasks"

    protected Project project

    protected Task signingTask
    protected Task validateTask
    protected Task zipAlignTask
    protected Task assembleWithPlatformKeyTask

    //Set of environment variable and reason to use.
    def environmentVarMap = ['PLATFORM_CERTIFICATE':'Set platform certificate location to your environment variable.',
                             'PLATFROM_PRIVATE_KEY':'Set private key file path to your environment variable.',
                             'SIGN_APK_JAR':'Set location of signapk jar to your environment variable.']

    protected void apply(Project project) {
        this.project = project

        project.android.applicationVariants.all { variant ->
            if(variant.buildType.name == 'release'){
                variant.outputs.each { output ->

                    def envcheck = project.tasks.create(name: "envCheck", type: EnvironmentVariableCheck)
                    envcheck.envMap = environmentVarMap

                    assembleWithPlatformKeyTask = project.tasks.create(name: "assembleWithPlatformKey${variant.buildType.name.capitalize()}")
                    assembleWithPlatformKeyTask.group = SYMBOL_GROUP
                    assembleWithPlatformKeyTask.description = "Build platform key signing task"

                    signingTask = project.tasks.create(name: "platformKey${variant.buildType.name.capitalize()}Sign", type: PlatformKeySign)
                    signingTask.inputFile = new File(output.outputFile.toString())
                    signingTask.certificateFile = new File(getCertificateFile())
                    signingTask.privateKeyFile = new File(getPrivateKeyFile())
                    signingTask.signApkJar = new File(getSignApkJarFile())
                    signingTask.outputFile = new File("${output.outputFile.parent}${File.separator}${getTempSignedApkName()}")

                    validateTask = project.tasks.create(name: "validatePlatform${variant.buildType.name.capitalize()}Signing", type: ValidateKeySigning)
                    validateTask.inputFile = signingTask.getOutputFile()

                    zipAlignTask = project.tasks.create(name: "zipAlignPlatform${variant.buildType.name.capitalize()}Signed", type: ZipAlignApk)
                    zipAlignTask.inputFile = signingTask.getOutputFile()
                    zipAlignTask.zipAlignExe = new File(getZipAlign())
                    zipAlignTask.outputFile = new File("${output.outputFile.parent}${File.separator}${getFinalApkName()}")
                    zipAlignTask.mustRunAfter(validateTask)

                    assembleWithPlatformKeyTask.dependsOn (envcheck,assemble,signingTask,validateTask,zipAlignTask)

                }
            }
        }

    }


    protected String getZipAlign(){
        def os = System.getProperty("os.name").toLowerCase()

        def zipAlign
        if (os.startsWith("Windows")) {
            zipAlign = "zipalign.exe";
        } else {
            zipAlign = "zipalign";
        }

        zipAlign = "${project.android.getSdkDirectory().getAbsolutePath()}${File.separator}build-tools${File.separator}${project.android.buildToolsVersion}${File.separator}${zipAlign}"
    }

    protected String getTempSignedApkName(){
        def fileName = "TempSigned.apk"

        def today = new Date()
        def sdf = new java.text.SimpleDateFormat("yyyyMMddhhmmss")

        fileName = fileName.replace(".", "-" + sdf.format(today) + ".")
    }

    protected String getCertificateFile() {
        System.getenv("PLATFORM_CERTIFICATE") ?: ""
    }

    protected String getPrivateKeyFile() {
        System.getenv("PLATFROM_PRIVATE_KEY") ?: ""
    }

    protected String getSignApkJarFile(){
        System.getenv("SIGN_APK_JAR") ?: ""
    }

    protected abstract String getFinalApkName()
}
