

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("trackerr.android.library")
    id("trackerr.android.hilt")
}

android {
    namespace = "io.rezyfr.trackerr.core.data"
}

dependencies {
    implementation(project(":core:persistence"))
    implementation(project(":core:domain"))
    // Arch Components
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.playservices.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    implementation(libs.androidx.dataStore.core)

    implementation(libs.arrow.kt.core)

    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
