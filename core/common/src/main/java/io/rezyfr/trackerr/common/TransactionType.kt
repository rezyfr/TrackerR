package io.rezyfr.trackerr.common

import androidx.annotation.StringDef

@StringDef(TransactionType.INCOME, TransactionType.EXPENSE)
annotation class TransactionType {
    companion object {
        const val INCOME = "income"
        const val EXPENSE = "expense"
    }
}

