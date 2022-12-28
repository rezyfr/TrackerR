package io.rezyfr.trackerr.core.domain.model

import io.rezyfr.trackerr.core.data.model.UserFirestore

data class UserModel(
    val id: String,
    val email: String,
    val name: String,
    val photoUrl: String,
)

fun UserFirestore.asDomainModel() = UserModel(
    id = id,
    email = email,
    name = name,
    photoUrl = photoUrl,
)