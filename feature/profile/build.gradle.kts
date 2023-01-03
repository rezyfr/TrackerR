

plugins {
    alias(libs.plugins.ksp)
    id("trackerr.android.feature")
    id("trackerr.android.library.compose")
}

android {
    namespace = "io.rezyfr.trackerr.feature.profile"

    ksp {
        arg("compose-destinations.moduleName", "profile")
        arg("compose-destinations.mode", "destinations")
    }
}

dependencies {

}