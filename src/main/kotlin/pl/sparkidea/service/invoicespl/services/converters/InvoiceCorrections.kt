package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.InvoiceCorrection
import pl.sparkidea.service.invoicespl.dto.InvoiceCorrectionDetailsResponse
import pl.sparkidea.service.invoicespl.dto.InvoiceCorrectionResponse
import reactor.core.publisher.Mono


/**
 *
 * @author Maciej Lesniak
 */

@Component
class InvoiceCorrectionToInvoiceCorrectionResponse : MonoConverter<InvoiceCorrection, InvoiceCorrectionResponse> {
    override fun convert(source: InvoiceCorrection): Mono<InvoiceCorrectionResponse> {

        return Mono.just(
                InvoiceCorrectionResponse(
                        source.oldVersionInvoice.id!!,
                        source.newVersionInvoice.id!!
                )
        )

    }
}

@Component
class InvoiceCorrectionToInvoiceCorrectionDetailsResponse(
        private val invoiceToInvoiceResponseConverter: InvoiceToInvoiceResponseConverter
) : MonoConverter<InvoiceCorrection, InvoiceCorrectionDetailsResponse> {
    override fun convert(source: InvoiceCorrection): Mono<InvoiceCorrectionDetailsResponse> {

        return Mono.zip(
                invoiceToInvoiceResponseConverter.convert(source.oldVersionInvoice),
                invoiceToInvoiceResponseConverter.convert(source.newVersionInvoice))
                .map {
                    InvoiceCorrectionDetailsResponse(
                            source.correctionNumber,
                            source.correctionDate,
                            it.t1,
                            it.t2
                    )
                }

    }
}