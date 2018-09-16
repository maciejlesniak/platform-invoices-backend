package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class BankAccount(
        val id: String,
        val accountName: String,
        val accountType: String,
        val accountNumber: String
) {
    companion object {
        fun empty(): BankAccount = BankAccount("", "", "", "")
    }
}