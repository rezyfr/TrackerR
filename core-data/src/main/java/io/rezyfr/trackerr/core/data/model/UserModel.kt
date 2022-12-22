package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName

data class UserModel(
    @get:PropertyName("email")
    val email: String,
    @get:PropertyName("name")
    val name: String,
)
