package io.rezyfr.trackerr.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.core.data.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState
    fun storeUserData(gsa: GoogleSignInAccount) {
        viewModelScope.launch {
            loginRepository.storeUserData(gsa).collectLatest {
                if (it.isSuccess) {
                    _uiState.value = LoginUiState.Success(true)
                } else {
                    _uiState.value = LoginUiState.Error(it.exceptionOrNull()!!)
                }
            }
        }
    }
}

sealed interface LoginUiState {
    object Loading : LoginUiState
    data class Error(val throwable: Throwable) : LoginUiState
    data class Success(val isSuccess: Boolean) : LoginUiState
}

