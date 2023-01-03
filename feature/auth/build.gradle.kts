

plugins {
    alias(libs.plugins.ksp)
    id("trackerr.android.feature")
    id("trackerr.android.library.compose")
}

android {
    namespace = "io.rezyfr.trackerr.feature.auth"

    ksp {
        arg("compose-destinations.moduleName", "auth")
        arg("compose-destinations.mode", "destinations")
    }
}

dependencies {
    implementation(libs.playservices.auth)
}