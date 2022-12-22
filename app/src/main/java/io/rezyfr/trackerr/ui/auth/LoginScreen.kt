package io.rezyfr.trackerr.ui.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
) {
    viewModel.uiState.collectAsState().value.let { state ->
        when (state) {
            is LoginUiState.Loading -> {
            }
            is LoginUiState.Error -> {
            }
            is LoginUiState.Success -> {
                onLogin()
            }
        }
    }
    SignInButton() {
        viewModel.storeUserData(it)
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
            containerColor = Color.Black,
            contentColor = Color.White
        )
    ) {
        Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
    }
}