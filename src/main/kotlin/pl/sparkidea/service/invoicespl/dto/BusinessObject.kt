package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Maciej Lesniak
 */
class NewBusinessObjectRequest(@JsonProperty(FULL_NAME) val fullName: String,
                               @JsonProperty(SHORT_NAME) val shortName: String,
                               @JsonProperty(NIP) val nip: String,
                               @JsonProperty(REGON) val regon: String,
                               @JsonProperty(ADDRESS) val address: AddressDto,
                               @JsonProperty(COORDINATOR_INFO) val coordinator: IndividualDto,
                               @JsonProperty(BANK_ACCOUNTS) val bankAccounts: List<BankAccountDto>,
                               @JsonProperty(IS_VAT_PAYER) val vatPayer: Boolean,
                               @JsonProperty(TAX_OFFICE_ID) val taxOfficeId: String) {
    companion object {
        const val FULL_NAME = "full_name"
        const val SHORT_NAME = "short_name"
        const val NIP = "nip"
        const val REGON = "regon"
        const val ADDRESS = "address"
        const val COORDINATOR_INFO = "coordinator"
        const val BANK_ACCOUNTS = "accounts"
        const val IS_VAT_PAYER = "vat_payer"
        const val TAX_OFFICE_ID = "tax_office_id"
    }
}

class BusinessObjectResponse(@JsonProperty(ID) var id: String,
                             @JsonProperty(FULL_NAME) val fullName: String,
                             @JsonProperty(SHORT_NAME) val shortName: String,
                             @JsonProperty(NIP) val nip: String,
                             @JsonProperty(REGON) val regon: String,
                             @JsonProperty(ADDRESS) val address: AddressDto,
                             @JsonProperty(COORDINATOR_INFO) val coordinator: IndividualDto,
                             @JsonProperty(BANK_ACCOUNTS) val bankAccounts: List<BankAccountDto>,
                             @JsonProperty(IS_VAT_PAYER) val vatPayer: Boolean,
                             @JsonProperty(TAX_OFFICE_ID) val taxOfficeId: String) {
    companion object {
        const val ID = "id"
        const val FULL_NAME = "full_name"
        const val SHORT_NAME = "short_name"
        const val NIP = "nip"
        const val REGON = "regon"
        const val ADDRESS = "address"
        const val COORDINATOR_INFO = "coordinator"
        const val BANK_ACCOUNTS = "accounts"
        const val IS_VAT_PAYER = "vat_payer"
        const val TAX_OFFICE_ID = "tax_office_id"
    }
}

class AddressDto(
        @JsonProperty(STREET) val street: String,
        @JsonProperty(HOUSE) val house: String,
        @JsonProperty(LOCAL) val local: String,
        @JsonProperty(CITY) val city: String,
        @JsonProperty(POSTAL_CODE) val postalCode: String,
        @JsonProperty(POSTAL_CODE_CITY) val postalCodeCity: String,
        @JsonProperty(COUNTRY_ISO_CODE) val countryIsoCode: String = "PL"
) {
    companion object {
        const val STREET = "street"
        const val HOUSE = "house"
        const val LOCAL = "local"
        const val POSTAL_CODE = "zip"
        const val POSTAL_CODE_CITY = "zip_city"
        const val CITY = "city"
        const val COUNTRY_ISO_CODE = "country"
    }
}

class IndividualDto(
        @JsonProperty(FIRST_NAME) val firstName: String,
        @JsonProperty(SECOND_NAME) val secondName: String,
        @JsonProperty(LAST_NAME) val lastName: String
) {
    companion object {
        const val FIRST_NAME = "first_name"
        const val SECOND_NAME = "second_name"
        const val LAST_NAME = "last_name"
    }
}