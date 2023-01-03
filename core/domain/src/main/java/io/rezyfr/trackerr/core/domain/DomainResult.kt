package io.rezyfr.trackerr.core.domain

import arrow.core.Either
import arrow.core.leftWiden
import arrow.core.right
import io.rezyfr.trackerr.core.domain.model.TrackerrError

typealias DomainResult<T> = Either<TrackerrError, T>
