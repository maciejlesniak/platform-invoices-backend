package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class Invoice(
        val id: String?,
        val payload: InvoicePayload,
        val metadata: InvoiceMetadata) {

    constructor(payload: InvoicePayload,
                metadata: InvoiceMetadata) : this(
            null,
            payload,
            metadata)
}