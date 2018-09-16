package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Maciej Lesniak
 */
class TaxOfficeRequest(
        @JsonProperty(TAX_OFFICE_NAME) val name: String,
        @JsonProperty(TAX_OFFICE_ADDRESS) val address: AddressDto,
        @JsonProperty(TAX_OFFICE_BANK_ACCOUNTS) val accounts: Set<BankAccountDto>
) {
    companion object {
        const val TAX_OFFICE_NAME = "name"
        const val TAX_OFFICE_ADDRESS = "address"
        const val TAX_OFFICE_BANK_ACCOUNTS = "accounts"
    }
}

class TaxOfficeResponse(
        @JsonProperty(TAX_OFFICE_ID) val id: String,
        @JsonProperty(TAX_OFFICE_NAME) val name: String,
        @JsonProperty(TAX_OFFICE_ADDRESS) val address: AddressDto,
        @JsonProperty(TAX_OFFICE_BANK_ACCOUNTS) val accounts: Set<BankAccountDto>
) {
    companion object {
        const val TAX_OFFICE_ID = "id"
        const val TAX_OFFICE_NAME = "name"
        const val TAX_OFFICE_ADDRESS = "address"
        const val TAX_OFFICE_BANK_ACCOUNTS = "accounts"
    }
}