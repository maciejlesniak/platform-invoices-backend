package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

/**
 * @author Maciej Lesniak
 */
@Document(collection = "pl_invoices")
class DbInvoice(@Indexed
                @Field(INVOICE_ID) var invoiceId: String,
                @Field(RECORDS) var records: Set<DbInvoiceRecord>,
                @Field(PAYMENT) var payment: DbPaymentInfo,
                @Field(CREATION_DATE) @Indexed var creationDate: LocalDate,
                @Field(SALE_DATE) @Indexed var saleDate: LocalDate,
                @Field(ISSUER_DATA_COPY) var issuerDataCopy: DbInvoiceBusinessObjectData,
                @Field(CONTRACTOR_DATA_COPY) var contractorDataCopy: DbInvoiceBusinessObjectData,
                @Field(INVOICE_NOTE) var invoiceNote: String,
                @Field(METADATA) var metadata: DbInvoiceMetadata
) {

    @Id
    lateinit var id: String

    @Version
    var version: Long = -1

    @Indexed
    @Field(INVOICE_CORRECTION_ID)
    var correctionId: String? = null

    @Field(INVOICE_CORRECTION_DATE)
    var correctionDate: LocalDate? = null

    fun withEmptyId() : DbInvoice {
        this.id = ""
        return this
    }

    companion object {

        const val RECORDS = "records"
        const val INVOICE_ID = "invoice_id"
        const val PAYMENT = "payment"
        const val CREATION_DATE = "created"
        const val SALE_DATE = "sold"
        const val ISSUER_DATA_COPY = "issuer_copy"
        const val CONTRACTOR_DATA_COPY = "contractor_copy"
        const val INVOICE_NOTE = "note"
        const val METADATA = "metadata"
        const val INVOICE_CORRECTION_ID = "correction_id"
        const val INVOICE_CORRECTION_DATE = "correction_date"

        fun withMetadata(obj: DbInvoice, metadata: DbInvoiceMetadata): DbInvoice {
            return DbInvoice(
                    obj.invoiceId,
                    obj.records,
                    obj.payment,
                    obj.creationDate,
                    obj.saleDate,
                    obj.issuerDataCopy,
                    obj.contractorDataCopy,
                    obj.invoiceNote,
                    metadata
            )
        }


    }

}

