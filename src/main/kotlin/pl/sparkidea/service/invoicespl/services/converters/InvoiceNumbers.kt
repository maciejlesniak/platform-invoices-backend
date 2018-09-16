package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.SequentialNumber
import pl.sparkidea.service.invoicespl.dto.InvoiceNumberResponse
import reactor.core.publisher.Mono
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 *
 * @author Maciej Lesniak
 */
@Component
class SequentialNumberToInvoiceNumberResponseConverter : MonoConverter<SequentialNumber, InvoiceNumberResponse> {

    // todo should be from users/businessUnit settings
    companion object {
        const val CORRECTIONS_NUMBER_TEMPLATE = "FC/{seq}/{date:MM}/{date:YYYY}"
        const val REGULAR_NUMBER_TEMPLATE = "FV/{seq}/{date:MM}/{date:YYYY}"
    }

    override fun convert(source: SequentialNumber): Mono<InvoiceNumberResponse> {

        val template = when (source.type) {
            SequentialNumber.Companion.SequenceType.CORRECTED_INVOICE_SEQUENCE_NAME -> CORRECTIONS_NUMBER_TEMPLATE
            SequentialNumber.Companion.SequenceType.REGULAR_INVOICE_SEQUENCE_NAME -> REGULAR_NUMBER_TEMPLATE
        }

        // todo should be with ZoneId from users/businessUnit settings
        val dateNow = LocalDate.now()

        val invoiceNumber = template
                .replace("{seq}", source.value.toString())
                .replace("{date:MM}", dateNow.format(DateTimeFormatter.ofPattern("MM")))
                .replace("{date:YYYY}", dateNow.format(DateTimeFormatter.ofPattern("YYYY")))

        return Mono.just(InvoiceNumberResponse(invoiceNumber))

    }
}