package com.symbol.build.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class EHSInstaller extends BasePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.afterEvaluate {
            super.apply(project)
        }

    }

    protected String getFinalApkName() {
        "${project.archivesBaseName}_${getFormattedVersionName()}.apk"
    }

    private String getFormattedVersionName(){
        project.versionName.split('\\.').collect{Integer.parseInt(it) < 10 ? "0${it}" : "${it}"}.join("")
    }
}
