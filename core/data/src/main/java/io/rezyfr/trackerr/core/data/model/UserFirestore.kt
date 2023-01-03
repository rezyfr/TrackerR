package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName
import io.rezyfr.trackerr.core.domain.model.UserModel

data class UserFirestore(
    @get:PropertyName("id")
    val id: String = "",
    @get:PropertyName("email")
    val email: String = "",
    @get:PropertyName("name")
    val name: String = "",
    @get:PropertyName("photoUrl")
    val photoUrl: String = "",
)


fun UserFirestore.asDomainModel() = UserModel(
    id = id,
    email = email,
    name = name,
    photoUrl = photoUrl,
)