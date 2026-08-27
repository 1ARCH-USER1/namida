allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven {
          url = project(":app").file("repo").toURI()
        }
    }
}

val newBuildDir: Directory =
    rootProject.layout.buildDirectory
        .dir("../../build")
        .get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)

    plugins.withId("com.android.library") {
        afterEvaluate {
            try {
                val android = project.extensions.getByType(com.android.build.gradle.LibraryExtension::class.java)
                if (android.namespace.isNullOrEmpty()) {
                    android.namespace = "com.${project.name.replace("-", ".").replace("_", ".")}"
                }
            } catch (_: Exception) {}
        }
    }
}

subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
