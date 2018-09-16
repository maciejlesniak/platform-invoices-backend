package pl.sparkidea.service.invoicespl.services.validators

import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */

abstract class MonoValidator<T> {
    open fun validate(subject: T): Mono<T> {
        throw NotImplementedError("validation must be implemented")
    }

    open fun validate(ownership: String, subject: T): Mono<T> {
        throw NotImplementedError("validation must be implemented")
    }
}

class ValidationException(override val message: String) : RuntimeException(message)