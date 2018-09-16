package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.mongodb.core.mapping.Field

/**
 *
 * @author Maciej Lesniak
 */
class DbInvoicePaymentBankAccount(
        @Field(ACCOUNT_TYPE) val accountType: String,
        @Field(ACCOUNT_NUMBER) val accountNumber: String
) {
    companion object {
        const val ACCOUNT_TYPE = "type"
        const val ACCOUNT_NUMBER = "number"

        fun empty(): DbInvoicePaymentBankAccount =
                DbInvoicePaymentBankAccount("", "")

    }

    fun isEmpty(): Boolean = accountNumber.isEmpty() && accountType.isEmpty()

}
