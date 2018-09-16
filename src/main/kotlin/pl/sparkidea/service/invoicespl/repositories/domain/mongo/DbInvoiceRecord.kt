package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.bson.types.Decimal128
import org.springframework.data.mongodb.core.mapping.Field

/**
 * @author Maciej Lesniak
 */
class DbInvoiceRecord(@Field(SUBJECT) val subject: String,
                      @Field(QUANTITY) val quantity: Decimal128,
                      @Field(NETT_UNIT_VALUE) val nettUnitValue: Decimal128,
                      @Field(TAX_BID_PRECENTAGE) val taxBidPrecentage: Decimal128,
                      @Field(MEASURE_UNITS) val measureUnits: String,
                      @Field(CURRENCY_ISO4217_SYMBOL) val currencyISO4217Symbol: String) {
    companion object {
        const val SUBJECT = "subject"
        const val QUANTITY = "quantity"
        const val NETT_UNIT_VALUE = "nett_unit_value"
        const val TAX_BID_PRECENTAGE = "tax"
        const val MEASURE_UNITS = "units"
        const val CURRENCY_ISO4217_SYMBOL = "currency"
    }
}
