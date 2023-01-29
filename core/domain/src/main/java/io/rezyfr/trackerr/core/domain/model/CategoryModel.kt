package io.rezyfr.trackerr.core.domain.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue


data class CategoryModel(
    val id: String,
    val type: String,
    val name: String,
    val userId: String,
    val color: Int,
    val icon: String,
    val iconRef: DocumentReference? = null
) {

    fun asAddCategoryFirestore(
        id: String? = null,
        uid: String,
        iconRef: DocumentReference
    ) = hashMapOf(
        "type" to type,
        "name" to name,
        "userId" to uid,
        "color" to color,
        "iconRef" to iconRef,
        "lastModified" to FieldValue.serverTimestamp()
    ).apply {
        if (!id.isNullOrEmpty()) {
            this["id"] = id
        }
    }
    companion object {
        fun emptyData() = CategoryModel(
            id = "",
            type = "",
            name = "",
            userId = "",
            color = 0,
            icon = ""
        )
    }
}
