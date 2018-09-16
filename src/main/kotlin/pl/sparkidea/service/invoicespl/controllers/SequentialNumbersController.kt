package pl.sparkidea.service.invoicespl.controllers

import org.springframework.web.bind.annotation.*
import pl.sparkidea.service.invoicespl.domain.BusinessRole
import pl.sparkidea.service.invoicespl.domain.SequentialNumber
import pl.sparkidea.service.invoicespl.dto.InvoiceNumberResponse
import pl.sparkidea.service.invoicespl.dto.SequentialNumberRequest
import pl.sparkidea.service.invoicespl.services.BusinessObjectsService
import pl.sparkidea.service.invoicespl.services.SequencesService
import pl.sparkidea.service.invoicespl.services.converters.SequentialNumberToInvoiceNumberResponseConverter
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@RestController
class SequentialNumbersController(
        private val sequencesService: SequencesService,
        private val businessObjectsService: BusinessObjectsService,
        private val sequentialNumberToInvoiceNumberResponseConverter: SequentialNumberToInvoiceNumberResponseConverter
) {

    @GetMapping("/issuer/{issuerId}/regular.invoice.number/last")
    fun getCurrentRegular(@PathVariable("issuerId") issuerId: String,
                          principal: Mono<Principal>):
            Mono<InvoiceNumberResponse> =
            principal
                    .flatMap {
                        sequencesService
                                .getValue(
                                        SequentialNumber.Companion.SequenceType.REGULAR_INVOICE_SEQUENCE_NAME,
                                        issuerId,
                                        it)
                                .flatMap { sequentialNumberToInvoiceNumberResponseConverter.convert(it) }
                    }

    @GetMapping("/issuer/{issuerId}/regular.invoice.number/next")
    fun getNextRegular(@PathVariable("issuerId") issuerId: String,
                       principal: Mono<Principal>):
            Mono<InvoiceNumberResponse> =
            principal
                    .flatMap {
                        sequencesService
                                .getValue(
                                        SequentialNumber.Companion.SequenceType.REGULAR_INVOICE_SEQUENCE_NAME,
                                        issuerId,
                                        it)
                                .map { SequentialNumber(it.type, it.value + 1L, it.businessUnitId) }
                                .flatMap { sequentialNumberToInvoiceNumberResponseConverter.convert(it) }
                    }

    @PatchMapping("/issuer/{issuerId}/regular.invoice.number/next")
    fun createNextRegular(@PathVariable("issuerId") issuerId: String,
                          principal: Mono<Principal>):
            Mono<InvoiceNumberResponse> =
            principal.flatMap {
                sequencesService
                        .commitNextReqularInvoiceNum(issuerId, it)
                        .flatMap { sequentialNumberToInvoiceNumberResponseConverter.convert(it) }
            }

    @GetMapping("/issuer/{issuerId}/correction.invoice.number/last")
    fun getCurrentCorrection(@PathVariable("issuerId") issuerId: String,
                             principal: Mono<Principal>):
            Mono<InvoiceNumberResponse> =
            principal.flatMap {
                sequencesService
                        .getValue(
                                SequentialNumber.Companion.SequenceType.CORRECTED_INVOICE_SEQUENCE_NAME,
                                issuerId,
                                it)
                        .flatMap { sequentialNumberToInvoiceNumberResponseConverter.convert(it) }
            }

    @GetMapping("/issuer/{issuerId}/correction.invoice.number/next")
    fun getNextCorrection(@PathVariable("issuerId") issuerId: String,
                          principal: Mono<Principal>):
            Mono<InvoiceNumberResponse> =
            principal
                    .flatMap {
                        sequencesService
                                .getValue(
                                        SequentialNumber.Companion.SequenceType.CORRECTED_INVOICE_SEQUENCE_NAME,
                                        issuerId,
                                        it)
                                .map { SequentialNumber(it.type, it.value + 1L, it.businessUnitId) }
                                .flatMap { sequentialNumberToInvoiceNumberResponseConverter.convert(it) }
                    }


    @PatchMapping("/issuer/{issuerId}/correction.invoice.number/next")
    fun createNextCorrection(@PathVariable("issuerId") issuerId: String,
                             principal: Mono<Principal>):
            Mono<InvoiceNumberResponse> =
            principal
                    .flatMap {
                        sequencesService
                                .commitNextCorrectionInvoiceNum(issuerId, it)
                                .flatMap { sequentialNumberToInvoiceNumberResponseConverter.convert(it) }
                    }


    @PatchMapping("/issuer/{issuerId}/reset")
    fun resetCorrection(@PathVariable("issuerId") issuerId: String,
                        @RequestBody resetToNumber: SequentialNumberRequest,
                        principal: Mono<Principal>):
            Mono<SequentialNumberRequest> =

            principal.flatMap { p ->

                businessObjectsService.findBusinessObjectByIdAndPrincipal(issuerId, p)
                        .flatMap {
                            when (it.businessRole) {
                                BusinessRole.ISSUER -> return@flatMap Mono.just(it.id!!)
                                else -> return@flatMap Mono.error<String>(Throwable("cannot find business object"))
                            }
                        }
                        .flatMap {
                            sequencesService.reset(
                                    SequentialNumber.Companion.SequenceType.valueOf(resetToNumber.type),
                                    it!!,
                                    p,
                                    resetToNumber.value)
                        }
                        .map { SequentialNumberRequest(it.type.name, it.value) }

            }


}