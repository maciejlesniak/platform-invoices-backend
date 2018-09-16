package pl.sparkidea.service.invoicespl.services

import org.springframework.stereotype.Service
import pl.sparkidea.service.invoicespl.domain.TaxOffice
import pl.sparkidea.service.invoicespl.dto.TaxOfficeResponse
import pl.sparkidea.service.invoicespl.repositories.TaxOfficesRepository
import pl.sparkidea.service.invoicespl.services.converters.DbTaxOfficeToTaxOfficeResponseConverter
import pl.sparkidea.service.invoicespl.services.converters.TaxOfficeToDbTaxOfficeConverter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@Service
class TaxOfficeService(private val taxOfficesRepository: TaxOfficesRepository,
                       private val taxOfficeToDbTaxOfficeConverter: TaxOfficeToDbTaxOfficeConverter,
                       private val dbTaxOfficeToTaxOfficeResponseConverter: DbTaxOfficeToTaxOfficeResponseConverter) {

    fun saveTaxOffice(taxOffice: TaxOffice, principal: Principal): Mono<TaxOfficeResponse> {
        return taxOfficeToDbTaxOfficeConverter.convert(taxOffice)
                .flatMap { taxOfficesRepository.save(it) }
                .flatMap { dbTaxOfficeToTaxOfficeResponseConverter.convert(it) }
    }

    fun getAllTaxOffices(): Flux<TaxOfficeResponse> {
        return taxOfficesRepository.findAll()
                .flatMap { dbTaxOfficeToTaxOfficeResponseConverter.convert(it) }
    }

    fun getTaxOfficeById(id: String): Mono<TaxOfficeResponse> {
        return taxOfficesRepository.findById(id)
                .flatMap { dbTaxOfficeToTaxOfficeResponseConverter.convert(it) }
    }

    fun deleteTaxOffice(id: String, principal: Principal): Mono<Long> {
        return taxOfficesRepository.deleteById(id).map { _: Void? -> 1L }
    }
}