package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
enum class PaymentMethod(methodName: String) {
    CASH("cash"),
    BANK_TRANSFER("bank_transfer")
}