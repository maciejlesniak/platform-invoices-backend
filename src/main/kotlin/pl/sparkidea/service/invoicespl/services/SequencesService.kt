package pl.sparkidea.service.invoicespl.services

import org.springframework.stereotype.Service
import pl.sparkidea.service.invoicespl.domain.SequentialNumber
import pl.sparkidea.service.invoicespl.repositories.DbNumeralSequenceDataTemplateRepository
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@Service
class SequencesService(
        private var dbNumeralSequenceDataTemplateRepository: DbNumeralSequenceDataTemplateRepository
) {

    companion object {
        const val FIRST_NUMBER = 0L
        val simpleSequencerNextValue: (Long) -> Long = { x -> x + 1L }
    }

    fun getValue(sequenceType: SequentialNumber.Companion.SequenceType,
                 businessUnitId: String,
                 principal: Principal): Mono<SequentialNumber> {
        return dbNumeralSequenceDataTemplateRepository
                .getValueOrInitIfNotFound(
                        sequenceType.name,
                        businessUnitId,
                        principal.name)
                .map { SequentialNumber(sequenceType, it!!, businessUnitId) }
    }

    fun commitNextReqularInvoiceNum(businessUnitId: String, principal: Principal): Mono<SequentialNumber> =
            persistNextNum(SequentialNumber.Companion.SequenceType.REGULAR_INVOICE_SEQUENCE_NAME, businessUnitId, principal)

    fun commitNextCorrectionInvoiceNum(businessUnitId: String, principal: Principal): Mono<SequentialNumber> =
            persistNextNum(SequentialNumber.Companion.SequenceType.CORRECTED_INVOICE_SEQUENCE_NAME, businessUnitId, principal)

    fun persistNextNum(sequenceType: SequentialNumber.Companion.SequenceType,
                       businessUnitId: String,
                       principal: Principal,
                       nextStrategy: (x: Long) -> Long = simpleSequencerNextValue): Mono<SequentialNumber> {
        return getValue(sequenceType, businessUnitId, principal)
                .flatMap { reset(it.type, businessUnitId, principal, nextStrategy(it.value)) }
    }

    fun reset(sequenceType: SequentialNumber.Companion.SequenceType,
              businessUnitId: String,
              principal: Principal,
              newValue: Long = FIRST_NUMBER): Mono<SequentialNumber> {
        return dbNumeralSequenceDataTemplateRepository
                .upsertSequence(
                        sequenceType.name,
                        businessUnitId,
                        principal.name,
                        newValue)
                .map { SequentialNumber(sequenceType, it, businessUnitId) }
    }

}

