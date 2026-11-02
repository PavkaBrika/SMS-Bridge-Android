pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://artifactory.appodeal.com/appodeal")
    }
}

rootProject.name = "SMS Bridge"
include(":app")
include(":data")
include(":domain")
