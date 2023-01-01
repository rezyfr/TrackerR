package io.rezyfr.trackerr.feature.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import io.rezyfr.trackerr.feature.auth.GoogleApiContract
import io.rezyfr.trackerr.feature.auth.LoginUiState
import io.rezyfr.trackerr.feature.auth.LoginViewModel

interface AuthNavigator {
    fun onLoginSuccess()
    fun navigateUp()
}
@RootNavGraph(start = true)
@Destination
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: AuthNavigator,
) {
    val loginState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen (
        state = loginState,
        onLogin = viewModel::storeUserData,
        onStoreUser = { navigator.onLoginSuccess() },
    )
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onLogin: (GoogleSignInAccount) -> Unit,
    onStoreUser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
        SignInButton(
            onLogin = onLogin
        )
        when (state) {
            is LoginUiState.Loading -> {
                Text("Loading")
            }
            is LoginUiState.Error -> {
                Text("Error: ${state.throwable.message}")
            }
            is LoginUiState.Success -> {
                onStoreUser.invoke()
            }
        }
    }
}

@Composable
fun SignInButton(
    onLogin: (GoogleSignInAccount) -> Unit = { },
) {
    val context = LocalContext.current
    val authResultLauncher =
        rememberLauncherForActivityResult(contract = GoogleApiContract()) { task ->
            try {
                val gsa = task?.getResult(ApiException::class.java)
                if (gsa != null) {
                    onLogin.invoke(gsa)
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    Button(
        onClick = {
            authResultLauncher.launch(1)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
    }
}