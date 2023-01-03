

package io.rezyfr.trackerr.feature.homescreen.ui.homescreen


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.feature.homescreen.ui.HomeScreenUiState
import io.rezyfr.trackerr.feature.homescreen.ui.HomeScreenViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class HomeScreenViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = HomeScreenViewModel(FakeTransactionRepository())
        assertEquals(viewModel.uiState.first(), HomeScreenUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = HomeScreenViewModel(FakeTransactionRepository())
        assertEquals(viewModel.uiState.first(), HomeScreenUiState.Loading)
    }
}

private class FakeTransactionRepository : TransactionRepository {

    private val data = mutableListOf<String>()

    override val homeScreens: Flow<List<String>>
        get() = flow { emit(data.toList()) }

    override suspend fun add(name: String) {
        data.add(0, name)
    }
}
