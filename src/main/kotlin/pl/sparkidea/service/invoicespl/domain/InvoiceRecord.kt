package pl.sparkidea.service.invoicespl.domain

import java.math.BigDecimal
import java.util.*

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceRecord(
        val subject: String,
        val measureUnits: String,
        val quantity: BigDecimal,
        val nettUnitValue: BigDecimal,
        val taxBidPrecentage: BigDecimal,
        val currency: Currency
) {

    // todo introduce operator overload

    val nettTotalValue: BigDecimal
        get() = quantity.multiply(nettUnitValue)

    val taxTotalValue: BigDecimal
        get() = grossTotalValue.subtract(nettTotalValue)

    val grossTotalValue: BigDecimal
        get() = nettTotalValue.multiply(taxBidPrecentage.add(BigDecimal.ONE))

}