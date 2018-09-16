package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Maciej Lesniak
 */
class BankAccountDto(
        @JsonProperty(ACCOUNT_NAME) val accountName: String,
        @JsonProperty(ACCOUNT_TYPE) val accountType: String,
        @JsonProperty(ACCOUNT_NUMBER) val accountNumber: String
) {

    @JsonProperty(ACCOUNT_ID)
    var accountId: String? = null

    companion object {
        const val ACCOUNT_ID = "acc_id"
        const val ACCOUNT_NAME = "name"
        const val ACCOUNT_TYPE = "type"
        const val ACCOUNT_NUMBER = "baseNumber"
    }

}