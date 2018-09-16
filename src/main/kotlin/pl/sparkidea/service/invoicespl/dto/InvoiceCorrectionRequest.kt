package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceCorrectionRequest(
        @JsonProperty(CORRECTION_INVOICE_NUMBER) val correctionNumber: String,
        @JsonProperty(CORRECTION_INVOICE_DATE) val correctionDate: LocalDate,
        @JsonProperty(CORRECTION_INVOICE_PAYLOAD) val invoiceRequest: InvoiceRequest
) {
    companion object {
        const val CORRECTION_INVOICE_NUMBER = "correction_invoice_number"
        const val CORRECTION_INVOICE_PAYLOAD = "new_payload"
        const val CORRECTION_INVOICE_DATE = "correction_date"
    }
}