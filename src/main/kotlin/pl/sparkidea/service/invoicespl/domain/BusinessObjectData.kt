package pl.sparkidea.service.invoicespl.domain


/**
 * Object that is a subject for business operations, could be individual or a company
 *
 * @author Maciej Lesniak
 */
class BusinessObjectData(
        var id: String?,
        val objFullName: String,
        val objShortName: String,
        val objNip: String,
        val objRegon: String,
        val objAddress: Address,
        val coordinatorInfoCopy: Individual,
        val bankAccounts: List<BankAccount>,
        val vatPayer: Boolean,
        val taxOfficeId: String,
        val businessRole: BusinessRole,
        val ownershipId: String
)

enum class BusinessRole {
    CONTRACTOR, ISSUER, UNKNOWN
}