package pl.sparkidea.service.invoicespl.domain

import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceCorrection (
        val correctionNumber:String,
        val correctionDate:LocalDate,
        val oldVersionInvoice: Invoice,
        val newVersionInvoice: Invoice
)