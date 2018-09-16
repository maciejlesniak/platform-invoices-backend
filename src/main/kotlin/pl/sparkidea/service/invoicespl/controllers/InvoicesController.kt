package pl.sparkidea.service.invoicespl.controllers

import org.springframework.web.bind.annotation.*
import pl.sparkidea.service.invoicespl.dto.InvoiceInfoResponse
import pl.sparkidea.service.invoicespl.dto.InvoiceRequest
import pl.sparkidea.service.invoicespl.services.BasicInvoicesService
import pl.sparkidea.service.invoicespl.services.converters.InvoiceToInvoiceResponseConverter
import pl.sparkidea.service.invoicespl.services.converters.NewInvoiceRequestToInvoiceConverter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@RestController
class InvoicesController(private val basicInvoicesService: BasicInvoicesService,
                         private val newInvoiceRequestToInvoiceConverter: NewInvoiceRequestToInvoiceConverter,
                         private val invoiceToInvoiceResponseConverter: InvoiceToInvoiceResponseConverter) {

    @GetMapping("/invoices")
    fun getAllInvoices(@RequestParam parameters: Map<String, String>,
                       principal: Mono<Principal>):
            Flux<InvoiceInfoResponse> =
            principal
                    .toFlux()
                    .flatMap { p ->
                        when {
                            parameters.isEmpty() -> basicInvoicesService.getAllInvoicesByOwner(p)
                            else -> basicInvoicesService.getAllInvoicesByOwnerWithParameters(p, parameters)
                        }.flatMap { invoiceToInvoiceResponseConverter.convert(it) }
                    }


    @GetMapping("/invoice/{id}")
    fun getInvoiceById(@PathVariable("id") id: String,
                       principal: Mono<Principal>):
            Mono<InvoiceInfoResponse> = principal
            .flatMap { p ->
                basicInvoicesService.getInvoiceById(id, p)
                        .flatMap { invoiceToInvoiceResponseConverter.convert(it) }
            }

    @PostMapping("/invoice")
    fun createInvoice(@RequestBody invoiceRequest: InvoiceRequest,
                      principal: Mono<Principal>):
            Mono<InvoiceInfoResponse> = principal.flatMap { p ->
        newInvoiceRequestToInvoiceConverter.convert(invoiceRequest, p)
                .flatMap { basicInvoicesService.createInvoice(it, p) }
                .flatMap { invoiceToInvoiceResponseConverter.convert(it) }
    }

    @PutMapping("/invoice/{id}/lock")
    fun changeInvoiceStatus(@PathVariable("id") id: String,
                            principal: Mono<Principal>):
            Mono<InvoiceInfoResponse> {
        TODO("lock invoice status change in basicInvoicesService and return updated invoice")
    }

    @PutMapping("/invoice/{id}")
    fun updateInvoice(@PathVariable("id") id: String,
                      @RequestBody newInvoiceRequest: InvoiceRequest,
                      principal: Mono<Principal>):
            Mono<InvoiceInfoResponse> {
        TODO("merge update for non-locked invoice")
    }

    @DeleteMapping("/invoice/{id}")
    fun deleteInvoice(@PathVariable("id") id: String,
                      principal: Mono<Principal>):
            Mono<Void> = principal
            .flatMap {
                basicInvoicesService
                        .deleteChengesAllowedInvoice(id, it)
                        .flatMap { Mono.empty<Void>() }
            }

}