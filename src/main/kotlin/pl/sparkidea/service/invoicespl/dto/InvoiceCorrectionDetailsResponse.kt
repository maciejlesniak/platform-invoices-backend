package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceCorrectionDetailsResponse(
        @JsonProperty(CORRECTION_INVOICE_NUMBER) val correctionNumber: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = InvoiceInfoResponse.DATE_FORMAT)
        @JsonProperty(CORRECTION_INVOICE_DATE) val correctionDate: LocalDate,
        @JsonProperty(OLD_INVOICE_PAYLOAD) val oldVersionInvoice: InvoiceInfoResponse,
        @JsonProperty(NEW_INVOICE_PAYLOAD) val newVersionInvoice: InvoiceInfoResponse) {

    companion object {
        const val CORRECTION_INVOICE_NUMBER = "correction_number"
        const val CORRECTION_INVOICE_DATE = "correction_date"
        const val OLD_INVOICE_PAYLOAD = "old_version"
        const val NEW_INVOICE_PAYLOAD = "new_version"
    }
}