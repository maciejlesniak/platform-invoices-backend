package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

/**
 *
 * @author Maciej Lesniak
 */
@Document(collection = "pl_tax_offices")
class DbTaxOffice(
        @Field(NAME) @Indexed var name: String,
        @Field(ADDRESS) var address: DbAddress,
        @Field(ACCOUNTS) var accounts: Set<DbBankAccount>
) {
    companion object {
        const val NAME = "name"
        const val ADDRESS = "address"
        const val ACCOUNTS = "accounts"
    }

    @Id
    lateinit var id: String

    @Version
    var version: Long = -1
}