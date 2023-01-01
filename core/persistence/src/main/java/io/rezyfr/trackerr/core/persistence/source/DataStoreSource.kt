package io.rezyfr.trackerr.core.persistence.source

import io.rezyfr.trackerr.core.persistence.datastore.DataStoreKeys
import io.rezyfr.trackerr.core.persistence.datastore.TrDataStore
import javax.inject.Inject

class DataStoreSource @Inject constructor(
    private val dataStore: TrDataStore,
    private val dataStoreKeys: DataStoreKeys
) {
    suspend fun doLogout() {
        dataStore.remove(dataStoreKeys.profile)
    }

    fun getUserProfile() = dataStore.get(dataStoreKeys.profile)
}