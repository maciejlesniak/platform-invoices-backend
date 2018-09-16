package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.InvoiceMetadata
import pl.sparkidea.service.invoicespl.dto.InvoiceStatusMetadataResponse
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */

@Component
class InvoiceMetadataToInvoiceStatusMetadataResponseConverter : MonoConverter<InvoiceMetadata, InvoiceStatusMetadataResponse> {
    override fun convert(source: InvoiceMetadata): Mono<InvoiceStatusMetadataResponse> {
        return Mono.just(InvoiceStatusMetadataResponse(
                source.lockStatus,
                source.baseDocId,
                source.correctedDocId
        ))
    }
}