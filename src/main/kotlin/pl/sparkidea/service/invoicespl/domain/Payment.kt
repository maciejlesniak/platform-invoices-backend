package pl.sparkidea.service.invoicespl.domain

import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class Payment (
    val method: PaymentMethod,
    val dueDate: LocalDate,
    val bankAccount: InvoicePaymentBankAccount?
)