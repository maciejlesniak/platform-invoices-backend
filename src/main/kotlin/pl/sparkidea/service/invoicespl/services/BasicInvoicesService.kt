package pl.sparkidea.service.invoicespl.services

import org.springframework.stereotype.Service
import pl.sparkidea.service.invoicespl.domain.Invoice
import pl.sparkidea.service.invoicespl.domain.LockStatus
import pl.sparkidea.service.invoicespl.domain.TimePeriod
import pl.sparkidea.service.invoicespl.repositories.InvoicesRepository
import pl.sparkidea.service.invoicespl.repositories.InvoicesTemplateRepository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoice
import pl.sparkidea.service.invoicespl.services.converters.DbInvoiceToInvoiceConverter
import pl.sparkidea.service.invoicespl.services.converters.InvoiceToDbInvoiceConverter
import pl.sparkidea.service.invoicespl.services.validators.NewInvoiceIdValidator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal
import java.time.Instant
import java.util.function.Predicate
import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author Maciej Lesniak
 */
@Service
class BasicInvoicesService(private val invoicesRepository: InvoicesRepository,
                           private val invoicesTemplateRepository: InvoicesTemplateRepository,
                           private val invoiceToDbInvoiceConverter: InvoiceToDbInvoiceConverter,
                           private val dbInvoiceToInvoiceConverter: DbInvoiceToInvoiceConverter,
                           private val newInvoiceIdValidator: NewInvoiceIdValidator,
                           private val sequencesService: SequencesService) {

    companion object {

        val log: Logger = Logger.getLogger(BasicInvoicesService::class.java.name)

        const val QUERY_PARAMETER_CREATED_AFTER_EPOCH_SECONDS = "createdAfter"
        const val QUERY_PARAMETER_CREATED_BEFORE_EPOCH_SECONDS = "createdBefore"
    }

    fun createInvoice(newInvoiceRequest: Invoice, principal: Principal): Mono<Invoice> {

        return newInvoiceIdValidator.validate(principal.name, newInvoiceRequest)
                .flatMap { validatedInvoice -> invoiceToDbInvoiceConverter.convert(validatedInvoice) }
                .flatMap { invoicesRepository.save(it) }
                .doOnSuccess {
                    sequencesService
                            .commitNextReqularInvoiceNum(
                                    newInvoiceRequest.payload.issuerDataCopy.id!!,
                                    principal)
                            .subscribe {
                                log.log(Level.FINER,"Regular invoice sequence number has just been committed to next value {0}", it)
                            }
                }
                .flatMap { dbInvoiceToInvoiceConverter.convert(it) }
    }

    fun getDbInvoiceByIdAndPrincipal(baseInvoiceId: String, principal: Principal): Mono<DbInvoice> {
        return invoicesRepository.findById(baseInvoiceId)
                .filter({ it.metadata.ownershipId == principal.name })
    }

    fun getInvoiceById(id: String, principal: Principal): Mono<Invoice> {
        return getDbInvoiceByIdAndPrincipal(id, principal)
                .flatMap { persistedInvoice -> dbInvoiceToInvoiceConverter.convert(persistedInvoice) }
    }

    fun getAllInvoicesByOwner(principal: Principal): Flux<Invoice> {
        return invoicesTemplateRepository.getAllCurrentInvoices(principal.name)
                .flatMap { persistedInvoice -> dbInvoiceToInvoiceConverter.convert(persistedInvoice) }
    }

    fun getAllInvoicesByOwnerAndCreationPeriod(principal: Principal, creationPeriod: TimePeriod): Flux<Invoice> {
        return invoicesRepository.findAllByMetadataOwnershipIdAndCreationDatePeriod(
                principal.name,
                creationPeriod.start,
                creationPeriod.end)
                .flatMap { persistedInvoice -> dbInvoiceToInvoiceConverter.convert(persistedInvoice) }
    }

    fun getAllInvoicesByOwnerWithParameters(principal: Principal, parameters: Map<String, String>): Flux<Invoice> {
        return if (isTimePeriodQuery().test(parameters)) {

            val periodStartInSeconds = parameters[QUERY_PARAMETER_CREATED_AFTER_EPOCH_SECONDS]?.toLong() ?: 0L
            val periodEndInSeconds = parameters[QUERY_PARAMETER_CREATED_BEFORE_EPOCH_SECONDS]?.toLong() ?: Instant.now().epochSecond

            val periodStart = Instant.ofEpochSecond(periodStartInSeconds)
            val periodEnd = Instant.ofEpochSecond(periodEndInSeconds)

            getAllInvoicesByOwnerAndCreationPeriod(principal, TimePeriod(periodStart, periodEnd))
        } else {
            getAllInvoicesByOwner(principal)
        }
    }

    fun deleteChengesAllowedInvoice(id: String, principal: Principal): Mono<Long> {
        return invoicesRepository
                .deleteByMetadataOwnershipIdAndIdAndMetadataLock(
                        principal.name,
                        id,
                        LockStatus.CHANGES_ALLOWED.name)
    }

    private fun isTimePeriodQuery(): Predicate<Map<String, String>> {
        return Predicate { t ->
            !t.isEmpty() && (t.containsKey(QUERY_PARAMETER_CREATED_AFTER_EPOCH_SECONDS) || t.containsKey(QUERY_PARAMETER_CREATED_BEFORE_EPOCH_SECONDS))
        }
    }

}