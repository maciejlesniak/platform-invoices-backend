package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import pl.sparkidea.service.invoicespl.domain.LockStatus
import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class InvoiceInfoResponse(
        @JsonProperty(DOCUMENT_ID) val id: String,
        @JsonProperty(INVOICE_NUMBER) val baseNumber: String,
        @JsonProperty(INVOICE_RECORDS) val records: List<InvoiceRecordResponse>,
        @JsonProperty(INVOICE_PAYMENT_METHOD) val paymentMethod: String,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
        @JsonProperty(INVOICE_PAYMENT_DUE_DATE) val paymentDueDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
        @JsonProperty(INVOICE_DATE_CREATED) val creationDate: LocalDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
        @JsonProperty(INVOICE_DATE_SOLD) val saleDate: LocalDate,
        @JsonProperty(INVOICE_ID_ISSUER) val issuerInfo: BusinessObjectResponse,
        @JsonProperty(INVOICE_ID_CONTRACTOR) val contractorInfo: BusinessObjectResponse,
        @JsonProperty(INVOICE_NOTE) val invoiceNote: String,
        @JsonProperty(INVOICE_METADATA) val statusMetadata: InvoiceStatusMetadataResponse,
        @JsonProperty(INVOICE_PAYMENT_BANK_ACCOUNT) val paymentBankAccountIban: String?
) {
    companion object {
        const val DOCUMENT_ID = "doc_id"
        const val INVOICE_NUMBER = "base_number"
        const val INVOICE_RECORDS = "records"
        const val INVOICE_PAYMENT_METHOD = "payment_method"
        const val INVOICE_PAYMENT_BANK_ACCOUNT = "payment_bank_account"
        const val INVOICE_PAYMENT_DUE_DATE = "payment_due_date"
        const val INVOICE_DATE_CREATED = "created"
        const val INVOICE_DATE_SOLD = "sold"
        const val INVOICE_ID_ISSUER = "issuer"
        const val INVOICE_ID_CONTRACTOR = "contractor"
        const val INVOICE_NOTE = "note"
        const val INVOICE_METADATA = "metadata_status"

        // todo date format to be globally setup
        const val DATE_FORMAT = "yyyy-MM-dd"
    }

}

class InvoiceRecordResponse(
        @JsonProperty(RECORD_SUBJECT) val subject: String,
        @JsonProperty(RECORD_QTY) val qty: Float,
        @JsonProperty(RECORD_UNIT_PRICE_NETT) val unitPrice: Float,
        @JsonProperty(RECORD_MEASURE_UNITS) val unitName: String,
        @JsonProperty(RECORD_TAX_BID) val taxBid: Float,
        @JsonProperty(CURRENCY_ISO4217_SYMBOL) val currencySymbol: String

) {
    companion object {
        const val RECORD_SUBJECT = "subject"
        const val RECORD_QTY = "qty"
        const val RECORD_UNIT_PRICE_NETT = "unit_price"
        const val RECORD_TAX_BID = "tax"
        const val RECORD_MEASURE_UNITS = "units"
        const val CURRENCY_ISO4217_SYMBOL  = "currency_symbol"
    }
}

class InvoiceStatusMetadataResponse(
        @JsonProperty(LOCK_STATUS) val locakStatus: LockStatus,
        @JsonProperty(BASE_DOC_ID) val baseDocId: String? = null,
        @JsonProperty(CORRECTED_DOC_ID) val correctedDocId: String? = null
) {
    companion object {
        const val LOCK_STATUS = "lock_status"
        const val CORRECTED_DOC_ID = "correction_doc_id"
        const val BASE_DOC_ID = "base_doc_id"
    }
}