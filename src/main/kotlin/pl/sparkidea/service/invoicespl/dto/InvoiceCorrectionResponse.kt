package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceCorrectionResponse(
        @JsonProperty(BASE_INVOICE_NUMBER) val baseInvoiceObjectId: String,
        @JsonProperty(CORRECTION_INVOICE) val correctionInvoiceObjectId: String
) {

    companion object {
        const val BASE_INVOICE_NUMBER = "base_doc_id"
        const val CORRECTION_INVOICE = "correction_invoice_doc_id"
    }

}