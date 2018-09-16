package pl.sparkidea.service.invoicespl.domain

import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class InvoicePayload (
    val invoiceNumber: String,
    val records: Collection<InvoiceRecord>,
    val payment: Payment,
    val creationDate: LocalDate,
    val saleDate: LocalDate,
    val issuerDataCopy: BusinessObjectData,
    val contractorDataCopy: BusinessObjectData,
    val invoiceNote: String
)