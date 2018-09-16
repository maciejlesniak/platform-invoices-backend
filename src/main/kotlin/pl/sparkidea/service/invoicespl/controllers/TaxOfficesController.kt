package pl.sparkidea.service.invoicespl.controllers

import org.springframework.web.bind.annotation.*
import pl.sparkidea.service.invoicespl.dto.TaxOfficeRequest
import pl.sparkidea.service.invoicespl.dto.TaxOfficeResponse
import pl.sparkidea.service.invoicespl.services.TaxOfficeService
import pl.sparkidea.service.invoicespl.services.converters.TaxOfficeRequestToTaxOfficeConverter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@RestController
class TaxOfficesController(
        private val taxOfficeService: TaxOfficeService,
        private val taxOfficeRequestToTaxOfficeConverter: TaxOfficeRequestToTaxOfficeConverter
) {
    @PostMapping("/taxoffice")
    fun create(@RequestBody request: TaxOfficeRequest, principal: Mono<Principal>):
            Mono<TaxOfficeResponse> =
            principal.flatMap { p ->
                taxOfficeRequestToTaxOfficeConverter.convert(request)
                        .flatMap { taxOfficeService.saveTaxOffice(it, p) }
            }


    @DeleteMapping("/taxoffice/{id}")
    fun delete(@PathVariable("id") id: String, principal: Mono<Principal>):
            Mono<Void> =
            principal.flatMap {
                taxOfficeService.deleteTaxOffice(id, it)
                        .flatMap { Mono.empty<Void>() }
            }


    @GetMapping("/taxoffices")
    fun getAll(): Flux<TaxOfficeResponse> = taxOfficeService.getAllTaxOffices()

    @GetMapping("/taxoffice/{id}")
    fun getById(@PathVariable("id") id: String): Mono<TaxOfficeResponse> = taxOfficeService.getTaxOfficeById(id)
}