package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName

data class UserFirestore(
    @get:PropertyName("id")
    val id: String,
    @get:PropertyName("email")
    val email: String,
    @get:PropertyName("name")
    val name: String,
    @get:PropertyName("photo_url")
    val photoUrl: String,
)
