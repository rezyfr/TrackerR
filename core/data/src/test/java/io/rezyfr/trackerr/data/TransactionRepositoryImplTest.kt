

package io.rezyfr.trackerr.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import io.rezyfr.trackerr.core.data.TransactionRepositoryImpl
import io.rezyfr.trackerr.core.persistence.room.HomeScreen
import io.rezyfr.trackerr.core.persistence.room.HomeScreenDao

/**
 * Unit tests for [TransactionRepositoryImpl].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class TransactionRepositoryImplTest {

    @Test
    fun homeScreens_newItemSaved_itemIsReturned() = runTest {
        val repository = TransactionRepositoryImpl(FakeHomeScreenDao())

        repository.add("Repository")

        assertEquals(repository.homeScreens.first().size, 1)
    }

}

private class FakeHomeScreenDao : HomeScreenDao {

    private val data = mutableListOf<HomeScreen>()

    override fun getHomeScreens(): Flow<List<HomeScreen>> = flow {
        emit(data)
    }

    override suspend fun insertHomeScreen(item: HomeScreen) {
        data.add(0, item)
    }
}
