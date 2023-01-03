

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("trackerr.android.library")
    id("trackerr.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.rezyfr.trackerr.core.persistence"
}

dependencies {
    // Arch Components
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.dataStore.preferences)

}
