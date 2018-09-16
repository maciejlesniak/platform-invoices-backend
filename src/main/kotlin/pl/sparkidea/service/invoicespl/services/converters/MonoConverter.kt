package pl.sparkidea.service.invoicespl.services.converters

import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
interface MonoConverter<S, T> {

    fun convert(source: S): Mono<T>

}