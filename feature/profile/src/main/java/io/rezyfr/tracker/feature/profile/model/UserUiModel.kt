package io.rezyfr.tracker.feature.profile.model

import io.rezyfr.trackerr.core.domain.model.UserModel

data class UserUiModel(
    val email: String,
    val name: String,
    val photoUrl: String,
)

fun UserModel.asUiModel() = UserUiModel(
    email = email,
    name = name,
    photoUrl = photoUrl,
)