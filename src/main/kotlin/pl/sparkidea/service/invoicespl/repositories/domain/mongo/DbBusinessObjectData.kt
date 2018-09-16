package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import pl.sparkidea.service.invoicespl.domain.BusinessRole

/**
 *
 * @author Maciej Lesniak
 */
@Document(collection = "pl_business_objects")
class DbBusinessObjectData(
        @Field(FULL_NAME) var objFullName: String,
        @Field(SHORT_NAME) var objShortName: String,
        @Field(NIP) var objNip: String,
        @Field(REGON) var objRegon: String,
        @Field(ADDRESS) var objAddress: DbAddress,
        @Field(INDIVIDUAL) var coordinatorInfoCopy: DbIndividual,
        @Field(BANK_ACCOUNT) var objIban: List<DbBankAccount>,
        @Field(VAT_PAYER) var vatPayer: Boolean,
        @Field(TAX_OFFICE) var taxOfficeId: String,
        @Field(OWNERSHIP) var ownershipId: String,
        @Field(BUSINESS_ROLE) var businessRole: BusinessRole
) {

    companion object {
        const val FULL_NAME = "full_name"
        const val SHORT_NAME = "short_name"
        const val NIP = "nip"
        const val REGON = "regon"
        const val ADDRESS = "address"
        const val INDIVIDUAL = "individual"
        const val BANK_ACCOUNT = "bank_account"
        const val VAT_PAYER = "vat_payer"
        const val TAX_OFFICE = "tax_office"
        const val OWNERSHIP = "ownership"
        const val BUSINESS_ROLE = "business_role"
    }

    @Id
    lateinit var id: String

    @Version
    var version: Long = -1

    fun getSafeInitializedIdValue(): String {
        try {
            return this.id
        } catch (ex: UninitializedPropertyAccessException) {
            return ""
        }
    }
}