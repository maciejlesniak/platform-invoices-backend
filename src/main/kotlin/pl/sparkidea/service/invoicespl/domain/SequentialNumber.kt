package pl.sparkidea.service.invoicespl.domain

/**
 *
 * @author Maciej Lesniak
 */
class SequentialNumber(
        val type: SequenceType,
        val value: Long,
        val businessUnitId: String // eg. issuer object id
) {
    companion object {
        enum class SequenceType {
            REGULAR_INVOICE_SEQUENCE_NAME,
            CORRECTED_INVOICE_SEQUENCE_NAME
        }
    }
}