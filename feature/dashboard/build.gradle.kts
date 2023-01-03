

plugins {
    alias(libs.plugins.ksp)
    id("trackerr.android.feature")
    id("trackerr.android.library.compose")
}

android {
    namespace = "io.rezyfr.trackerr.feature.dashboard"

    ksp {
        arg("compose-destinations.moduleName", "dashboard")
        arg("compose-destinations.mode", "destinations")
    }
}

dependencies {
    implementation(project(":feature:transaction"))
}