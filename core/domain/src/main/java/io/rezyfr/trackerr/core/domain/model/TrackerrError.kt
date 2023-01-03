package io.rezyfr.trackerr.core.domain.model

data class TrackerrError(
  override val message: String
) : Throwable()

fun Throwable.toError(): TrackerrError {
    return TrackerrError(this.message.orEmpty())
}