package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceRequest(
        @JsonProperty(INVOICE_NUMBER) val number: String,
        @JsonProperty(INVOICE_RECORDS) val records: List<InvoiceRecordRequest>,
        @JsonProperty(INVOICE_PAYMENT_METHOD) val paymentMethod: String,
        @JsonProperty(INVOICE_PAYMENT_DUE_DATE) val paymentDueDate: LocalDate,
        @JsonProperty(INVOICE_DATE_CREATED) val creationDate: LocalDate,
        @JsonProperty(INVOICE_DATE_SOLD) val saleDate: LocalDate,
        @JsonProperty(INVOICE_ID_ISSUER) val issuerId: String,
        @JsonProperty(INVOICE_ID_CONTRACTOR) val contractorId: String,
        @JsonProperty(INVOICE_NOTE) val invoiceNote: String,
        @JsonProperty(INVOICE_ISSUER_BANK_ACCOUNT) val issuerIban: String?
) {
    companion object {
        const val INVOICE_NUMBER = "invoice_number"
        const val INVOICE_RECORDS = "records"
        const val INVOICE_PAYMENT_METHOD = "payment_method"
        const val INVOICE_PAYMENT_DUE_DATE = "payment_due_date"
        const val INVOICE_DATE_CREATED = "created"
        const val INVOICE_DATE_SOLD = "sold"
        const val INVOICE_ID_ISSUER = "issuerId"
        const val INVOICE_ID_CONTRACTOR = "contractorId"
        const val INVOICE_NOTE = "note"
        const val INVOICE_ISSUER_BANK_ACCOUNT = "issuer_iban"
    }

}

class InvoiceRecordRequest(
        @JsonProperty(RECORD_SUBJECT) val subject: String,
        @JsonProperty(RECORD_QTY) val qty: Float,
        @JsonProperty(RECORD_UNIT_PRICE_NETT) val unitPrice: Float,
        @JsonProperty(RECORD_MEASURE_UNITS) val unitName: String,
        @JsonProperty(RECORD_TAX_BID) val taxBid: Float,
        @JsonProperty(CURRENCY_ISO4217_SYMBOL) val currencyCode: String) {

    companion object {
        const val RECORD_SUBJECT = "subject"
        const val RECORD_QTY = "qty"
        const val RECORD_UNIT_PRICE_NETT = "unit_price"
        const val RECORD_TAX_BID = "tax"
        const val RECORD_MEASURE_UNITS = "units"
        const val CURRENCY_ISO4217_SYMBOL  = "currency_symbol"
    }

}