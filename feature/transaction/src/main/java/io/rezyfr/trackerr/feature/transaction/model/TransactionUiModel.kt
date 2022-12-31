package io.rezyfr.trackerr.feature.transaction.model

import androidx.compose.ui.text.input.TextFieldValue
import io.rezyfr.trackerr.common.TransactionType
import io.rezyfr.trackerr.core.domain.mapper.formatToUi
import io.rezyfr.trackerr.core.domain.model.CategoryModel
import io.rezyfr.trackerr.core.domain.model.TransactionModel
import io.rezyfr.trackerr.core.domain.model.WalletModel
import java.time.LocalDate

data class TransactionUiModel(
    val id: String,
    val amount: Long,
    val amountLabel: TextFieldValue,
    val date: String,
    val description: String,
    @TransactionType val type: String,
    val wallet: WalletModel,
    val category: CategoryModel,
) {
    val isIncome = type == TransactionType.INCOME

    companion object {
        fun emptyData() = TransactionUiModel(
            id = "",
            amount = 0L,
            amountLabel = TextFieldValue(),
            date = LocalDate.now().formatToUi(),
            description = "",
            type = TransactionType.EXPENSE,
            wallet = WalletModel.emptyData(),
            category = CategoryModel.emptyData(),
        )
    }
}

fun TransactionModel.asUiModel() =
    TransactionUiModel(
        id = id,
        amount = amount,
        amountLabel = TextFieldValue(amount.toString()),
        date = date.formatToUi(),
        description = description,
        type = type,
        wallet = wallet,
        category = category,
    )
