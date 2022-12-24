package io.rezyfr.trackerr.core.data.model

import com.google.firebase.firestore.PropertyName

data class CategoryModel(
    @get:PropertyName("category")
    val category: String = "",
    @get:PropertyName("name")
    val name: String = "",
)
