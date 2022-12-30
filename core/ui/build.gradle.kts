/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    id("trackerr.android.library")
    id("trackerr.android.library.compose")
}

android {
    namespace = "io.rezyfr.trackerr.core.ui"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.runtime.livedata)
//    api(libs.androidx.metrics)
//    api(libs.androidx.tracing.ktx)
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.6")

    // Compose
    api(libs.androidx.compose.material3)
    implementation("dev.chrisbanes.snapper:snapper:0.3.0")
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.activity.compose)
    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)
}
