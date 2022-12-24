package io.rezyfr.trackerr.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.core.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        viewModelScope.launch {
            authRepository.isLoggedIn.collectLatest {
                if (it) _uiState.value = LoginUiState.Success
            }
        }
    }

    fun storeUserData(gsa: GoogleSignInAccount) {
        viewModelScope.launch {
            authRepository.storeUserData(gsa).collectLatest {
                if (it.isSuccess) {
                    _uiState.value = LoginUiState.Success
                } else {
                    _uiState.value =
                        LoginUiState.Error(it.exceptionOrNull() ?: Exception("Unknown error"))                }
            }
        }
    }
}

sealed interface LoginUiState {
    object Loading : LoginUiState
    data class Error(val throwable: Throwable) : LoginUiState
    object Success : LoginUiState
}

