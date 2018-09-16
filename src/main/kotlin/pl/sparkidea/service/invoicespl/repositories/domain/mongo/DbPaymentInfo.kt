package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDate

/**
 *
 * @author Maciej Lesniak
 */
class DbPaymentInfo(
        @Field(PEYMENT_METHOD) val peymentMethod: String,
        @Field(DUE_DATE) val dueDate: LocalDate,
        @Field(BANK_ACCOUNT_COPY) val bankAccountCopy: DbInvoicePaymentBankAccount?
) {
    companion object {
        const val PEYMENT_METHOD = "method"
        const val DUE_DATE = "due"
        const val BANK_ACCOUNT_COPY = "payment_account"
    }
}