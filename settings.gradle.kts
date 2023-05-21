

pluginManagement {
    listOf(repositories, dependencyResolutionManagement.repositories).forEach {
        it.apply {
            mavenCentral()
            google()
            maven(url = "https://androidx.dev/storage/compose-compiler/repository")
            maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
            gradlePluginPortal {
                content {
                }
            }
            maven {
                url = uri("https://jitpack.io")
            }
        }
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "TrackerR"

include(":app")
include(":core:common")
include(":core:data")
include(":core:domain")
include(":core:persistence")
include(":core:testing")
include(":core:ui")
include(":feature:auth")
include(":feature:transaction")
include(":feature:dashboard")
include(":feature:profile")
include(":feature:wallet")
include(":feature:category")

