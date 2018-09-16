package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Field
import pl.sparkidea.service.invoicespl.domain.LockStatus

/**
 *
 * @author Maciej Lesniak
 */
class DbInvoiceMetadata(
        @Indexed
        @Field(OWNERSHIP_ID) val ownershipId: String,
        @Field(LOCK) val lock: LockStatus,
        @Field(INVOICE_BASE_VERSION_OBJ_ID)var baseVersion: ObjectId? = null,
        @Field(INVOICE_CORRECTED_VERSION_OBJ_ID) var correctedVersion: ObjectId? = null
) {
    companion object {

        const val OWNERSHIP_ID = "ownership_id"
        const val LOCK = "lock"
        const val INVOICE_BASE_VERSION_OBJ_ID = "version_base"
        const val INVOICE_CORRECTED_VERSION_OBJ_ID = "version_corrected"

        fun withBaseVersion(obj: DbInvoiceMetadata, baseVersion: ObjectId): DbInvoiceMetadata {
            return DbInvoiceMetadata(
                    obj.ownershipId,
                    obj.lock,
                    baseVersion,
                    obj.correctedVersion
            )
        }

        fun withCorrectedVersion(obj: DbInvoiceMetadata, correctedVersion: ObjectId): DbInvoiceMetadata {
            return DbInvoiceMetadata(
                    obj.ownershipId,
                    obj.lock,
                    obj.baseVersion,
                    correctedVersion
            )
        }
    }
}