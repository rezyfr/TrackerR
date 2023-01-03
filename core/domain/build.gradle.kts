

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("trackerr.android.library")
    id("trackerr.android.hilt")
}

android {
    namespace = "io.rezyfr.trackerr.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    testImplementation(project(":core:testing"))

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.6")

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.playservices.auth)
    implementation(libs.kotlinx.coroutines.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    implementation(libs.arrow.kt.core)
    // Local tests: jUnit, coroutines, Android runner
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}
