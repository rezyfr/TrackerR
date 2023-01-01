package io.rezyfr.tracker.feature.profile.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import io.rezyfr.tracker.feature.profile.model.UserUiModel
import io.rezyfr.tracker.feature.profile.model.asUiModel
import io.rezyfr.trackerr.common.ResultState
import io.rezyfr.trackerr.common.asResult
import io.rezyfr.trackerr.core.domain.usecase.GetCurrentUserProfileUseCase
import io.rezyfr.trackerr.core.domain.usecase.LogoutUseCase
import io.rezyfr.trackerr.core.ui.base.SimpleFlowViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getCurrentUserProfileUseCase: GetCurrentUserProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : SimpleFlowViewModel<ProfileViewState, ProfileEvent>() {

    private val _logoutResult: MutableStateFlow<LogoutViewState> = MutableStateFlow(LogoutViewState.Loading)
    val logoutResult: StateFlow<LogoutViewState> = _logoutResult

    override val initialUi: ProfileViewState = ProfileViewState.Loading

    override val uiFlow: Flow<ProfileViewState> =
            getCurrentUserProfileUseCase.invoke(Unit).asResult().map {
            when (it) {
                is ResultState.Success -> {
                    ProfileViewState.Success(it.data.asUiModel())
                }
                is ResultState.Error -> {
                    ProfileViewState.Error(it.exception ?: Exception())
                }
                else -> {
                    ProfileViewState.Loading
                }
            }
        }

    override suspend fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Init -> {
                // do nothing
            }
            is ProfileEvent.DoLogout -> {
                logoutUseCase.invoke(Unit).asResult().collectLatest {
                    _logoutResult.value = when (it) {
                        is ResultState.Success -> {
                            LogoutViewState.Success
                        }
                        is ResultState.Error -> {
                            LogoutViewState.Error(it.exception ?: Exception())
                        }
                        else -> {
                            LogoutViewState.Loading
                        }
                    }
                }
            }
        }
    }
}


sealed interface ProfileViewState {
    object Loading : ProfileViewState
    data class Success(val data: UserUiModel) : ProfileViewState
    data class Error(val exception: Throwable) : ProfileViewState
}

sealed interface LogoutViewState {
    object Idle : LogoutViewState
    object Loading : LogoutViewState
    object Success : LogoutViewState
    data class Error(val exception: Throwable) : LogoutViewState
}

sealed interface ProfileEvent {
    object Init : ProfileEvent
    object DoLogout : ProfileEvent
}

