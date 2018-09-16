package pl.sparkidea.service.invoicespl.controllers

import org.springframework.web.bind.annotation.*
import pl.sparkidea.service.invoicespl.domain.BusinessRole
import pl.sparkidea.service.invoicespl.dto.BusinessObjectResponse
import pl.sparkidea.service.invoicespl.dto.NewBusinessObjectRequest
import pl.sparkidea.service.invoicespl.services.BusinessObjectsService
import pl.sparkidea.service.invoicespl.services.converters.BusinessObjectDataToInfoConverter
import pl.sparkidea.service.invoicespl.services.converters.NewBusinessObjectRequestToBusinessObjectConverter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@RestController
class BusinessObjectsController(
        private val newBusinessObjectRequestConverter: NewBusinessObjectRequestToBusinessObjectConverter,
        private val businessObjectDataToBusinessObjectResponseConverter: BusinessObjectDataToInfoConverter,
        private val businessObjectsService: BusinessObjectsService
) {

    @PostMapping("/contractor")
    fun createContractor(@RequestBody request: NewBusinessObjectRequest, principal: Mono<Principal>):
            Mono<BusinessObjectResponse> =

            principal.flatMap {
                newBusinessObjectRequestConverter
                        .convert(request, Pair(it, BusinessRole.CONTRACTOR))
                        .flatMap { businessObjectsService.createBusinessObject(it) }
                        .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
            }


    @PostMapping("/issuer")
    fun createIssuer(@RequestBody request: NewBusinessObjectRequest, principal: Mono<Principal>):
            Mono<BusinessObjectResponse> =

            principal
                    .flatMap {
                        newBusinessObjectRequestConverter
                                .convert(request, Pair(it, BusinessRole.ISSUER))
                                .flatMap { businessObjectsService.createBusinessObject(it) }
                                .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
                    }


    @DeleteMapping("/bobj/{id}")
    fun delete(@PathVariable("id") id: String, principal: Mono<Principal>):
            Mono<Void> =

            principal
                    .flatMap {
                        businessObjectsService
                                .deleteBusinessObject(id, it)
                                .flatMap { Mono.empty<Void>() }
                    }

    @GetMapping("/contractors")
    fun getAllContractors(principal: Mono<Principal>):
            Flux<BusinessObjectResponse> =

            principal
                    .toFlux()
                    .flatMap {
                        businessObjectsService
                                .getAllContractorsByOwner(it)
                                .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
                    }

    @GetMapping("/issuers")
    fun getAllIssuers(principal: Mono<Principal>):
            Flux<BusinessObjectResponse> =

            principal
                    .toFlux()
                    .flatMap {
                        businessObjectsService
                                .getAllIssuersByOwner(it)
                                .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
                    }

    @GetMapping("/bobj/{id}")
    fun getBusinessObjectById(@PathVariable("id") id: String, principal: Mono<Principal>):
            Mono<BusinessObjectResponse> =

            principal.flatMap {
                businessObjectsService
                        .findBusinessObjectByIdAndPrincipal(id, it)
                        .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
            }


    @PutMapping("/contractor/{id}")
    fun updateContractorById(@PathVariable("id") id: String,
                             @RequestBody request: NewBusinessObjectRequest,
                             principal: Mono<Principal>):
            Mono<BusinessObjectResponse> =

            principal.flatMap { p ->
                newBusinessObjectRequestConverter
                        .convert(request, Pair(p, BusinessRole.CONTRACTOR))
                        .flatMap { businessObjectsService.updateBusinessObject(id, it, p) }
                        .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
            }


    @PutMapping("/issuer/{id}")
    fun updateIssuerById(@PathVariable("id") id: String,
                         @RequestBody request: NewBusinessObjectRequest,
                         principal: Mono<Principal>):
            Mono<BusinessObjectResponse> =

            principal.flatMap { p ->
                newBusinessObjectRequestConverter
                        .convert(request, Pair(p, BusinessRole.ISSUER))
                        .flatMap { businessObjectsService.updateBusinessObject(id, it, p) }
                        .flatMap { businessObjectDataToBusinessObjectResponseConverter.convert(it) }
            }


}