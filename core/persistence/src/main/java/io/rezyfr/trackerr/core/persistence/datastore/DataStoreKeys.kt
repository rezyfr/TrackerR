package io.rezyfr.trackerr.core.persistence.datastore

import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreKeys @Inject constructor() {
    val profile by lazy { stringPreferencesKey(name = "profile") }
}