

plugins {
    alias(libs.plugins.ksp)
    id("trackerr.android.feature")
    id("trackerr.android.library.compose")
}

android {
    namespace = "io.rezyfr.trackerr.feature.category"

    ksp {
        arg("compose-destinations.moduleName", "transaction")
        arg("compose-destinations.mode", "destinations")
    }
}

dependencies {
    implementation(libs.firebase.firestore)
}
