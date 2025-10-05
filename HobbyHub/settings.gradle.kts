pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // 🔑 FIXED: Added JitPack repository to resolve the image cropper dependency
        maven { url = java.net.URI("https://www.jitpack.io") }
    }
}

rootProject.name = "HobbyHub"
include(":app")