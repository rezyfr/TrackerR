

package io.rezyfr.trackerr.core.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HomeScreen::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeScreenDao(): HomeScreenDao
}
