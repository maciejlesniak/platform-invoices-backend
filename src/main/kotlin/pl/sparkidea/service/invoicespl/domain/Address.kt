package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class Address(
        val street: String,
        val house: String,
        val local: String,
        val city: String,
        val postalCode: String,
        val postOfficeCity: String,
        val countryIsoCode: String = "PL"
)
