package pl.sparkidea.service.invoicespl.controllers

import org.springframework.web.bind.annotation.*
import pl.sparkidea.service.invoicespl.domain.InvoiceCorrection
import pl.sparkidea.service.invoicespl.dto.InvoiceCorrectionDetailsResponse
import pl.sparkidea.service.invoicespl.dto.InvoiceCorrectionRequest
import pl.sparkidea.service.invoicespl.dto.InvoiceCorrectionResponse
import pl.sparkidea.service.invoicespl.services.BasicInvoicesService
import pl.sparkidea.service.invoicespl.services.InvoiceCorrectionsService
import pl.sparkidea.service.invoicespl.services.converters.InvoiceCorrectionToInvoiceCorrectionDetailsResponse
import pl.sparkidea.service.invoicespl.services.converters.InvoiceCorrectionToInvoiceCorrectionResponse
import pl.sparkidea.service.invoicespl.services.converters.NewInvoiceRequestToInvoiceConverter
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@RestController
class InvoicesCorrectionsController(private val basicInvoicesService: BasicInvoicesService,
                                    private val invoiceCorrectionsService: InvoiceCorrectionsService,
                                    private val newInvoiceRequestToInvoiceConverter: NewInvoiceRequestToInvoiceConverter,
                                    private val invoiceCorrectionToInvoiceCorrectionResponse: InvoiceCorrectionToInvoiceCorrectionResponse,
                                    private val invoiceCorrectionToInvoiceCorrectionDetailsResponse: InvoiceCorrectionToInvoiceCorrectionDetailsResponse
) {

    @PostMapping("/invoice/{id}/correction")
    fun correctInvoice(@PathVariable("id") id: String,
                       @RequestBody invoiceCorrectionRequest: InvoiceCorrectionRequest,
                       principal: Mono<Principal>):
            Mono<InvoiceCorrectionResponse> = principal.flatMap { p ->

        val correctionNumber = Mono.just(invoiceCorrectionRequest.correctionNumber)
        val correctionDate = Mono.just(invoiceCorrectionRequest.correctionDate)
        val newInvoiceDomainVersion = newInvoiceRequestToInvoiceConverter.convert(invoiceCorrectionRequest.invoiceRequest, p)
        val oldInvoiceDomainVersion = basicInvoicesService.getInvoiceById(id, p)

        return@flatMap Mono.zip(
                correctionNumber,
                correctionDate,
                oldInvoiceDomainVersion,
                newInvoiceDomainVersion)
                .map { InvoiceCorrection(it.t1, it.t2, it.t3, it.t4) }
                .flatMap { invoiceCorrectionsService.createInvoiceCorrection(it, p) }
                .flatMap { invoiceCorrectionToInvoiceCorrectionResponse.convert(it) }
    }

    @GetMapping("/correction/{id}")
    fun getCorrectionInvoice(@PathVariable("id") id: String,
                             principal: Mono<Principal>):
            Mono<InvoiceCorrectionDetailsResponse> = principal.flatMap { p ->
        return@flatMap invoiceCorrectionsService.getInvoiceCorrection(id, p)
                .flatMap { invoiceCorrectionToInvoiceCorrectionDetailsResponse.convert(it) }

    }

}