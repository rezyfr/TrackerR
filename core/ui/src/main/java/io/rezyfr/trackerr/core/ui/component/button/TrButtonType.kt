package io.rezyfr.trackerr.core.ui.component.button

sealed interface TrButtonType {
    object Primary : TrButtonType
    object Danger: TrButtonType
}