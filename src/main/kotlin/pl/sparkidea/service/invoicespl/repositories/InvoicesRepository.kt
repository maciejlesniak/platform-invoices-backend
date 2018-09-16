package pl.sparkidea.service.invoicespl.repositories

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoice
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

/**
 * @author Maciej Lesniak
 */
interface InvoicesRepository : ReactiveCrudRepository<DbInvoice, String> {
    fun findAllByMetadataOwnershipId(ownershipId: String): Flux<DbInvoice>

    @Query(value = "{\"metadata.ownershipId\" : ?0, \"creationDate\":{ \"\$gt\":?1, \"\$lt\":?2 }}")
    fun findAllByMetadataOwnershipIdAndCreationDatePeriod(ownerId: String,
                                              creationDateAfter: Instant,
                                              creationDateBefore: Instant): Flux<DbInvoice>

    fun findAllByMetadataOwnershipIdAndInvoiceId(ownershipId: String, invoiceId: String): Flux<DbInvoice>

    fun findByIdAndMetadataOwnershipId(documentId: String, ownerId: String): Mono<DbInvoice>

    fun deleteByMetadataOwnershipIdAndIdAndMetadataLock(ownerId: String, invoiceId: String, lock: String): Mono<Long>

}
