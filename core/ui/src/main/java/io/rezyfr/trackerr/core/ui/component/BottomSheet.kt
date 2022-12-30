package io.rezyfr.trackerr.core.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import io.rezyfr.trackerr.core.ui.DarkPurpleGray95
import io.rezyfr.trackerr.core.ui.PurpleGray80

const val EXPAND = 1
const val COLLAPSE = 0

var showBottomSheet = COLLAPSE

data class BottomSheet(
    val visibilityState: MutableState<Boolean> = mutableStateOf(false)
) {
    fun collapse() {
        visibilityState.value = false
        showBottomSheet = COLLAPSE
    }

    fun expand() {
        visibilityState.value = true
        showBottomSheet = EXPAND
    }
}

@Composable
fun rememberBottomSheetShowState(): BottomSheet = remember { BottomSheet() }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BoxScope.TrxBottomSheet(
    modifier: Modifier = Modifier,
    bottomSheet: BottomSheet,
    level: Int = 1,
    content: @Composable () -> Unit
) {
    val visible by bottomSheet.visibilityState
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1_000f * level),
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(PurpleGray80.copy(alpha = 0.95f))
                .testTag("modal_outside_blur")
                .clickable(
                    onClick = {
                        keyboardController?.hide()
                        bottomSheet.collapse()
                    },
                    enabled = visible
                )
        )
    }
    AnimatedModalBottomSheetTransition(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .zIndex(1_100f * level),
        visible = visible
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            BackHandler(enabled = bottomSheet.visibilityState.value) {
                bottomSheet.collapse()
            }
            content()
        }
    }
}