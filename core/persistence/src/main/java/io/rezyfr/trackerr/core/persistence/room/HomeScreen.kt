

package io.rezyfr.trackerr.core.persistence.room

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class HomeScreen(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface HomeScreenDao {
    @Query("SELECT * FROM homescreen ORDER BY uid DESC LIMIT 10")
    fun getHomeScreens(): Flow<List<HomeScreen>>

    @Insert
    suspend fun insertHomeScreen(item: HomeScreen)
}
