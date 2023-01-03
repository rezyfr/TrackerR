@file:OptIn(ExperimentalLifecycleComposeApi::class)

package io.rezyfr.tracker.feature.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import io.rezyfr.tracker.feature.profile.model.UserUiModel
import io.rezyfr.trackerr.core.ui.TrTheme
import io.rezyfr.trackerr.core.ui.component.ButtonText
import io.rezyfr.trackerr.core.ui.component.CircleImage
import io.rezyfr.trackerr.core.ui.component.FullSpacer
import io.rezyfr.trackerr.core.ui.component.VSpacer
import io.rezyfr.trackerr.core.ui.component.button.TrDangerButton

interface ProfileNavigator {
    fun onLogoutSuccess()
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
@Destination
fun ProfileScreen(
    navigator: ProfileNavigator
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val logoutState by viewModel.logoutResult.collectAsStateWithLifecycle()
    ProfileScreenContent(
        state = state,
        onLogoutClick = {
            viewModel.onEvent(ProfileEvent.DoLogout)
        },
    )
    LogoutEffect(logoutState, onLogoutResult = { navigator.onLogoutSuccess() })
}

@Composable
fun LogoutEffect(state: LogoutViewState, onLogoutResult: () -> Unit = {}) {
    LaunchedEffect(key1 = state, block = {
        when (state) {
            is LogoutViewState.Loading -> {
                // show loading
            }
            is LogoutViewState.Error -> {
                // show error
            }
            is LogoutViewState.Success -> {
                onLogoutResult()
            }
            LogoutViewState.Idle -> Unit
        }
    })
}

@Composable
fun ProfileScreenContent(
    state: ProfileViewState,
    onLogoutClick: () -> Unit = {}
) {
    when (state) {
        is ProfileViewState.Loading -> {

        }
        is ProfileViewState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleImage(url = state.data.photoUrl, size = 100.dp)
                VSpacer(16.dp)
                Text(text = state.data.name, style = MaterialTheme.typography.titleLarge)
                VSpacer(4.dp)
                Text(text = state.data.email, style = MaterialTheme.typography.bodyLarge)
                FullSpacer()
                LogoutButton(onLogoutClick = onLogoutClick)
            }
        }
        is ProfileViewState.Error -> {

        }
    }
}

@Composable
fun LogoutButton(onLogoutClick: () -> Unit) {
    TrDangerButton(
        onClick = {
            onLogoutClick.invoke()
        },
        text = "Logout",
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewProfile() {
    TrTheme {
        ProfileScreenContent(
            state = ProfileViewState.Success(
                data = UserUiModel(
                    name = "Fidriyanto Rizkillah",
                    email = "frizkillah98@gmail.com",
                    photoUrl = "https://lh3.googleusercontent.com/a/AEdFTp45oBYXyei183tTlYUjAeQbdt1nBEIZRS0-Om4a=s96-c"
                ),
            ),
        )
    }
}