package pl.sparkidea.service.invoicespl.services

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import pl.sparkidea.service.invoicespl.domain.InvoiceCorrection
import pl.sparkidea.service.invoicespl.domain.LockStatus
import pl.sparkidea.service.invoicespl.domain.SequentialNumber
import pl.sparkidea.service.invoicespl.repositories.InvoicesRepository
import pl.sparkidea.service.invoicespl.repositories.InvoicesTemplateRepository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoice
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoiceMetadata
import pl.sparkidea.service.invoicespl.services.converters.DbInvoiceToInvoiceConverter
import pl.sparkidea.service.invoicespl.services.converters.InvoiceToDbInvoiceConverter
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.security.Principal
import java.util.logging.Level

/**
 *
 * @author Maciej Lesniak
 */
@Service
class InvoiceCorrectionsService(
        private val invoicesRepository: InvoicesRepository,
        private val invoiceToDbInvoiceConverter: InvoiceToDbInvoiceConverter,
        private val dbInvoiceToInvoiceConverter: DbInvoiceToInvoiceConverter,
        private val invoicesTemplateRepository: InvoicesTemplateRepository,
        private val sequencesService: SequencesService) {

    fun getInvoiceCorrection(documentId: String, principal: Principal): Mono<InvoiceCorrection> {

        val principalName = principal.name

        return this.invoicesRepository.findByIdAndMetadataOwnershipId(documentId, principalName)
                .flatMap { dbCorrectionInvoice: DbInvoice ->
                    val correctionInvoiceNumber = dbCorrectionInvoice.correctionId!!
                    val correctionInvoiceDate = dbCorrectionInvoice.correctionDate!!
                    val baseDocumentId = dbCorrectionInvoice.metadata.baseVersion!!

                    this.invoicesRepository.findByIdAndMetadataOwnershipId(baseDocumentId.toString(), principalName)
                            .flatMap { dbBaseInvoice: DbInvoice ->
                                Mono.just(Tuples.of(correctionInvoiceNumber, correctionInvoiceDate, dbBaseInvoice, dbCorrectionInvoice))
                            }
                }
                .flatMap {
                    Mono.zip(
                            Mono.just(it.t1),
                            Mono.just(it.t2),
                            dbInvoiceToInvoiceConverter.convert(it.t3),
                            dbInvoiceToInvoiceConverter.convert(it.t4))
                            .map {
                                InvoiceCorrection(
                                        it.t1,
                                        it.t2,
                                        it.t3,
                                        it.t4)
                            }
                }

    }

    /**
     * * checks if metadata.correctedVersion is NOT present (do not proceed if present)
     * * updates old invoice metadata to locked
     * * saves new invoice (with old invoice id reference in the metadata) and get its id
     * * updates old invoice metadata with new invoice id
     *  TODO error management
     */
    fun createInvoiceCorrection(correction: InvoiceCorrection, principal: Principal): Mono<InvoiceCorrection> {

        fun baseVersionLockOnOldInvoice() = invoicesTemplateRepository
                .lock(correction.oldVersionInvoice.id.toString(), LockStatus.CHANGES_NOT_ALLOWED)

        fun saveNewInvoice() =
                invoiceToDbInvoiceConverter.convert(correction.newVersionInvoice)
                .map { dbNewInvoice: DbInvoice ->
                    val updatedMetadata = DbInvoiceMetadata.withBaseVersion(
                            dbNewInvoice.metadata,
                            ObjectId(correction.oldVersionInvoice.id))

                    val correctionInvoice = DbInvoice.withMetadata(dbNewInvoice, updatedMetadata)
                    correctionInvoice.correctionId = correction.correctionNumber
                    correctionInvoice.correctionDate = correction.correctionDate

                    return@map correctionInvoice
                }

                .flatMap { invoicesRepository.save(it) }

        fun updateOldInvoice(correctedVersionId: String): Mono<DbInvoice> = invoicesRepository
                .findByIdAndMetadataOwnershipId(correction.oldVersionInvoice.id!!, principal.name)
                .flatMap {
                    val updatedMetadata = DbInvoiceMetadata.withCorrectedVersion(it.metadata, ObjectId(correctedVersionId))
                    return@flatMap invoicesTemplateRepository.updateInvoiceMetadata(it.id, updatedMetadata)
                }

        fun updateSequenceNumber(): Mono<SequentialNumber> = sequencesService
                .commitNextCorrectionInvoiceNum(correction.newVersionInvoice.payload.issuerDataCopy.id!!, principal)

        return Mono.zip(
                baseVersionLockOnOldInvoice(),
                saveNewInvoice())
                .flatMap { invoicePair: Tuple2<DbInvoice, DbInvoice> ->
                    updateOldInvoice(invoicePair.t2.id)
                            .map { Tuples.of(it, invoicePair.t2) }
                }
                .doOnSuccess {
                    updateSequenceNumber()
                            .subscribe {
                                BasicInvoicesService.log.log(Level.FINER, "Correction invoice sequence number has just been committed to next value {0}", it)
                            }
                }
                .flatMap {
                    Mono.zip(
                            dbInvoiceToInvoiceConverter.convert(it.t1),
                            dbInvoiceToInvoiceConverter.convert(it.t2))
                            .map {
                                InvoiceCorrection(
                                        correction.correctionNumber,
                                        correction.correctionDate,
                                        it.t1,
                                        it.t2)
                            }
                }

    }

}