package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class InvoicePaymentBankAccount(
        val accountType: String,
        val accountNumber: String
) {
    companion object {
        fun empty(): InvoicePaymentBankAccount = InvoicePaymentBankAccount("", "")
    }
    fun isEmpty(): Boolean = this.accountNumber.isEmpty() && this.accountType.isEmpty()
}