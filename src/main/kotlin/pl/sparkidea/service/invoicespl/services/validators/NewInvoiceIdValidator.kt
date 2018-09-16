package pl.sparkidea.service.invoicespl.services.validators

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.Invoice
import pl.sparkidea.service.invoicespl.repositories.InvoicesRepository
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */

@Component
class NewInvoiceIdValidator(
        private val invoicesRepository: InvoicesRepository
) : MonoValidator<Invoice>() {

    override fun validate(ownership: String, subject: Invoice): Mono<Invoice> {

        // todo count in db or in service
        return invoicesRepository
                .findAllByMetadataOwnershipIdAndInvoiceId(ownership, subject.payload.invoiceNumber)
                .count()
                .map { cnt ->
                    if (cnt != 0L) {
                        val invoiceId = subject.payload.invoiceNumber
                        throw ValidationException("duplicated invoice <<$invoiceId>>")
                    }

                    return@map subject
                }
    }
}