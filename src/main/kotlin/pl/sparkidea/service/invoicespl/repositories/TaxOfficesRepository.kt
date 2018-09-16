package pl.sparkidea.service.invoicespl.repositories

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbTaxOffice

/**
 *
 * @author Maciej Lesniak
 */
interface TaxOfficesRepository : ReactiveCrudRepository<DbTaxOffice, String>