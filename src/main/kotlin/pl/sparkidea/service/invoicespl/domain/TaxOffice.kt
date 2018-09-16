package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class TaxOffice(
        val name: String,
        val address: Address,
        val accounts: Set<BankAccount>
)