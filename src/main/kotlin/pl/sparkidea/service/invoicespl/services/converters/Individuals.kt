package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.Individual
import pl.sparkidea.service.invoicespl.dto.IndividualDto
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbIndividual
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
@Component
class IndividualToDbIndividualConverter : MonoConverter<Individual, DbIndividual> {
    override fun convert(source: Individual): Mono<DbIndividual> {
        return Mono.just(DbIndividual(source.firstName, source.secondName, source.lastName))
    }
}

@Component
class DbIndividualToIndividualConverter : MonoConverter<DbIndividual, Individual> {
    override fun convert(source: DbIndividual): Mono<Individual> {
        return Mono.just(Individual(source.firstName, source.secondName, source.lastName))
    }
}

@Component
class IndividualToIndividualDtoConverter : MonoConverter<Individual, IndividualDto> {
    override fun convert(source: Individual): Mono<IndividualDto> {
        return Mono.just(IndividualDto(
                source.firstName,
                source.secondName,
                source.lastName
        ))
    }
}

@Component
class IndividualDtoToIndividualConverter : MonoConverter<IndividualDto, Individual> {
    override fun convert(source: IndividualDto): Mono<Individual> {
        return Mono.just(Individual(
                source.firstName,
                source.secondName,
                source.lastName
        ))
    }
}