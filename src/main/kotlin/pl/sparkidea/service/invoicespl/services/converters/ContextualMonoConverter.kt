package pl.sparkidea.service.invoicespl.services.converters

import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
interface ContextualMonoConverter<S, T, C> {

    fun convert(source: S, converterContext:C): Mono<T>

}