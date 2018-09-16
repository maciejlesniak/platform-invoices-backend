package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.mongodb.core.mapping.Field

/**
 *
 * @author Maciej Lesniak
 */
class DbBankAccount(
        @Field(ACCOUNT_ID) val id: String,
        @Field(ACCOUNT_NAME) val accountName: String,
        @Field(ACCOUNT_TYPE) val accountType: String,
        @Field(ACCOUNT_NUMBER) val accountNumber: String
) {
    companion object {
        const val ACCOUNT_ID = "acc_id"
        const val ACCOUNT_NAME = "name"
        const val ACCOUNT_TYPE = "type"
        const val ACCOUNT_NUMBER = "number"

        fun empty() = DbBankAccount("", "", "", "")

    }

}