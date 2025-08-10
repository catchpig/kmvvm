val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows()
println("isWindows:$isWindows")
if (isWindows) {
    pluginManagement {
        repositories {
            maven {
                url = uri("https://maven.aliyun.com/repository/central")
            }
            maven {
                url = uri("https://maven.aliyun.com/repository/public/")
            }
            maven {
                url = uri("https://maven.aliyun.com/repository/gradle-plugin")
            }
            maven {
                url = uri("https://mirrors.cloud.tencent.com/repository/maven-public/")
            }
            gradlePluginPortal()
            mavenCentral()
            google()
        }
    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            maven {
                url = uri("https://www.jitpack.io")
            }
            maven {
                url = uri("https://maven.aliyun.com/repository/central")
            }
            maven {
                url = uri("https://maven.aliyun.com/repository/public/")
            }
            maven {
                url = uri("https://maven.aliyun.com/repository/gradle-plugin")
            }
            maven {
                url = uri("https://mirrors.cloud.tencent.com/repository/maven-public/")
            }

            mavenCentral()
            google()
        }
    }
} else {
    pluginManagement {
        repositories {
            google()
            mavenCentral()
            gradlePluginPortal()
            maven { url = uri("https://jitpack.io") }
        }
    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            maven { url = uri("https://jitpack.io") }
        }
    }
}


rootProject.name = "kmvvm"
include (":app")
include (":mvvm")
include (":annotation")
include (":compiler")
include (":download")
include (":utils")
