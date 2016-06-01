package com.tutbuild


import com.tutbuild.tasks.GitCheckoutTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.GradleBuild

/**
 * This class is to prepare EHS artifacts.
 */
class EnterpriseHomeScreen implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println project.tutworkspace
        //Lets Clone com.tutbuild.EnterpriseHomeScreen
        //def grgit = Grgit.clone(dir: "${project.tutworkspace}${File.separator}${project.EHSFolderName}", uri: "${project.gitEnterpriseHomeScreen}")

        def ehsBuildTask = project.tasks.create(name: "generateEhsArtifacts")
        ehsBuildTask.group = "Symbol Build"

        def  versionProperties = new Properties();
        File versionFile = new File('/Users/home/tut/tutbuild/buildSrc/version.properties')
        versionFile.withInputStream {
            versionProperties.load(it)
        }

        def ehsGitCheckoutTask = project.tasks.create(name: "CheckoutEHS", type: GitCheckoutTask)
        ehsGitCheckoutTask.repoLocation = "${project.gitEnterpriseHomeScreen}"
        ehsGitCheckoutTask.cloneLocation = "${project.tutworkspace}${File.separator}${project.EHSFolderName}"
        ehsGitCheckoutTask.commitId = "${versionProperties.enterprisehomescreen}"

        def buildTask = project.tasks.create(name: "buildEnterpriseHomeScreen", type: GradleBuild)
        buildTask.dir = "${project.tutworkspace}${File.separator}${project.EnterpriseHomeScreenPath}"
        buildTask.tasks = ["assembleWithPlatformKeyRelease"] as String[]

        def signedApkLocation = "${project.tutworkspace}${File.separator}${project.EnterpriseHomeScreenPath}${File.separator}app${File.separator}build${File.separator}outputs${File.separator}apk${File.separator}PLATFORMSIGNEDAPK"
        def copyEhsApkTask = project.tasks.create(name: "copyEhsArtifacts", type: Copy){
            from "${signedApkLocation}"
            into "${project.tutworkspace}${File.separator}output"
        }

        def buildEhsMotTask = project.tasks.create(name: "buildEnterpriseHomeScreen-Mot", type: GradleBuild)
        buildEhsMotTask.dir = "${project.tutworkspace}${File.separator}${project.EnterpriseHomeScreenMotPath}"
        buildEhsMotTask.tasks = ["assembleWithPlatformKeyRelease"] as String[]

        def signedMotApkLocation = "${project.tutworkspace}${File.separator}${project.EnterpriseHomeScreenMotPath}${File.separator}app${File.separator}build${File.separator}outputs${File.separator}apk${File.separator}PLATFORMSIGNEDAPK"
        def copyEhsMotApkTask = project.tasks.create(name: "copyEhsMotArtifacts", type: Copy){
            from "${signedMotApkLocation}"
            into "${project.tutworkspace}${File.separator}output"
        }

        ehsBuildTask.dependsOn(ehsGitCheckoutTask,buildTask,copyEhsApkTask)

    }
}