package io.rezyfr.trackerr.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.trackerr.core.domain.repository.UserRepository
import io.rezyfr.trackerr.core.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        viewModelScope.launch {
            userRepository.isLoggedIn.collectLatest {
                if (it) _uiState.value = LoginUiState.Success
            }
        }
    }

    fun storeUserData(gsa: GoogleSignInAccount) {
        viewModelScope.launch {
            loginUseCase(gsa).collectLatest {
                if (it.isSuccess) {
                    _uiState.value = LoginUiState.Success
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
    object Success : LoginUiState
}

