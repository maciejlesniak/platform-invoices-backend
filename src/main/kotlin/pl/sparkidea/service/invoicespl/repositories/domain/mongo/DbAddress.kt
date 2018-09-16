package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.mongodb.core.mapping.Field

/**
 *
 * @author Maciej Lesniak
 */
class DbAddress(
        @Field(STREET) val street: String,
        @Field(HOUSE) val house: String,
        @Field(LOCAL) val local: String,
        @Field(CITY) val city: String,
        @Field(POSTAL_CODE) val postalCode: String,
        @Field(POSTAL_CODE_CITY) val postalCodeCity: String,
        @Field(COUNTRY_ISO_CODE) val countryIsoCode: String = "PL"
) {
    companion object {
        const val STREET = "street"
        const val HOUSE = "house"
        const val LOCAL = "local"
        const val CITY = "city"
        const val POSTAL_CODE = "zip"
        const val POSTAL_CODE_CITY = "zip_city"
        const val COUNTRY_ISO_CODE = "country"

    }
}