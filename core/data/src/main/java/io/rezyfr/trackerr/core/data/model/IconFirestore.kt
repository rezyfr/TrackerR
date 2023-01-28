package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.IconModel

data class IconFirestore(
    @get:PropertyName("url")
    val url: String = "",
)
fun IconFirestore.asString() = url
