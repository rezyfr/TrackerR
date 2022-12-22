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

package io.rezyfr.trackerr.feature.homescreen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import io.rezyfr.trackerr.core.data.HomeScreenRepository
import io.rezyfr.trackerr.feature.homescreen.ui.HomeScreenUiState.Error
import io.rezyfr.trackerr.feature.homescreen.ui.HomeScreenUiState.Loading
import io.rezyfr.trackerr.feature.homescreen.ui.HomeScreenUiState.Success
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenRepository: HomeScreenRepository
) : ViewModel() {

    val uiState: StateFlow<HomeScreenUiState> = homeScreenRepository
        .homeScreens.map { Success(data = it) }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addHomeScreen(name: String) {
        viewModelScope.launch {
            homeScreenRepository.add(name)
        }
    }
}

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Error(val throwable: Throwable) : HomeScreenUiState
    data class Success(val data: List<String>) : HomeScreenUiState
}
