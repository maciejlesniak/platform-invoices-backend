package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceMetadata(
        val ownershipId: String,
        val lockStatus: LockStatus = LockStatus.CHANGES_ALLOWED,
        val baseDocId: String? = null,
        val correctedDocId: String? = null
)