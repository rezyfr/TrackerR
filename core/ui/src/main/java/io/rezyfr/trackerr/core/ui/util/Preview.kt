package io.rezyfr.trackerr.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel

@Composable
inline fun <reified VM : ViewModel> hiltViewModelPreviewSafe(): VM? =
    if (isInPreview()) null else hiltViewModel()

@Composable
fun isInPreview(): Boolean = LocalInspectionMode.current