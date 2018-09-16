package pl.sparkidea.service.invoicespl.repositories

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import pl.sparkidea.service.invoicespl.domain.LockStatus
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoice
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoiceMetadata
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
@Repository
class InvoicesTemplateRepository(
        private val reactiveMongoTemplate: ReactiveMongoTemplate
) {

    fun lock(objectId: String, newStatus: LockStatus): Mono<DbInvoice> {
        return reactiveMongoTemplate.findAndModify(
                Query(Criteria
                        .where("_id")
                        .`is`(objectId)
                ),
                Update.update("${DbInvoice.METADATA}.${DbInvoiceMetadata.LOCK}", newStatus.name),
                DbInvoice::class.java)
    }

    fun getAllCurrentInvoices(ownershipId: String): Flux<DbInvoice> {
        return reactiveMongoTemplate.find(
                Query(Criteria
                        .where("${DbInvoice.METADATA}.${DbInvoiceMetadata.INVOICE_CORRECTED_VERSION_OBJ_ID}")
                        .exists(false)
                        .and("${DbInvoice.METADATA}.${DbInvoiceMetadata.OWNERSHIP_ID}")
                        .`is`(ownershipId)
                ),
                DbInvoice::class.java
        )
    }

    fun isBaseInvoice(objId: String): Mono<Boolean> {
        return reactiveMongoTemplate
                .exists(
                        Query(Criteria
                                .where("_id")
                                .`is`(objId)
                                .and("${DbInvoice.METADATA}.${DbInvoiceMetadata.INVOICE_CORRECTED_VERSION_OBJ_ID}")
                                .exists(false)
                        ),
                        DbInvoice::class.java
                )
    }

    fun updateInvoiceMetadata(objId: String, newMetadata: DbInvoiceMetadata): Mono<DbInvoice> {
        return reactiveMongoTemplate
                .findAndModify(
                        Query(Criteria
                                .where("_id")
                                .`is`(objId)
                        ),
                        Update.update(DbInvoice.METADATA, newMetadata),
                        DbInvoice::class.java
                )
    }

}