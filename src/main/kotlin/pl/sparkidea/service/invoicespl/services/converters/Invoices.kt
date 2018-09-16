package pl.sparkidea.service.invoicespl.services.converters

import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.*
import pl.sparkidea.service.invoicespl.dto.InvoiceInfoResponse
import pl.sparkidea.service.invoicespl.dto.InvoiceRequest
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoice
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoicePaymentBankAccount
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbPaymentInfo
import pl.sparkidea.service.invoicespl.services.BusinessObjectsService
import reactor.core.publisher.Mono
import reactor.util.function.Tuples
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@Component
class NewInvoiceRequestToInvoiceConverter(
        var businessObjectsService: BusinessObjectsService,
        var invoiceRecordsRequestToInvoiceRecordsConverter: InvoiceRecordsRequestToInvoiceRecordsConverter
) : ContextualMonoConverter<InvoiceRequest, Invoice, Principal> {

    override fun convert(source: InvoiceRequest, converterContext: Principal): Mono<Invoice> {
        return Mono.zip(
                invoiceRecordsRequestToInvoiceRecordsConverter.convert(source.records),
                businessObjectsService.findBusinessObjectByIdAndPrincipal(source.issuerId, converterContext),
                businessObjectsService.findBusinessObjectByIdAndPrincipal(source.contractorId, converterContext)
        )
                .map {
                    Invoice(InvoicePayload(
                            source.number,
                            it.t1,
                            Payment(
                                    PaymentMethod.valueOf(source.paymentMethod),
                                    source.paymentDueDate,
                                    if (source.issuerIban != null) {
                                        InvoicePaymentBankAccount("iban", source.issuerIban)
                                    } else {
                                        null
                                    }
                            ),
                            source.creationDate,
                            source.saleDate,
                            it.t2,
                            it.t3,
                            source.invoiceNote
                    ), InvoiceMetadata(converterContext.name))
                }
    }


}

@Component
class InvoiceToInvoiceResponseConverter(
        val businessObjectDataToInfoConverter: BusinessObjectDataToInfoConverter,
        val invoiceMetadataToInvoiceStatusMetadataResponseConverter: InvoiceMetadataToInvoiceStatusMetadataResponseConverter,
        val invoiceRecordsToInvoiceRecordInfosResponseConverter: InvoiceRecordsToInvoiceRecordInfosResponseConverter
) : MonoConverter<Invoice, InvoiceInfoResponse> {

    override fun convert(source: Invoice): Mono<InvoiceInfoResponse> {
        return Mono.zip(
                invoiceRecordsToInvoiceRecordInfosResponseConverter.convert(source.payload.records),
                invoiceMetadataToInvoiceStatusMetadataResponseConverter.convert(source.metadata),
                businessObjectDataToInfoConverter.convert(source.payload.issuerDataCopy),
                businessObjectDataToInfoConverter.convert(source.payload.contractorDataCopy))
                .map {
                    InvoiceInfoResponse(
                            source.id ?: "",
                            source.payload.invoiceNumber,
                            it.t1.toList(),
                            source.payload.payment.method.name,
                            source.payload.payment.dueDate,
                            source.payload.creationDate,
                            source.payload.saleDate,
                            it.t3,
                            it.t4,
                            source.payload.invoiceNote,
                            it.t2,
                            source.payload.payment.bankAccount?.accountNumber)
                }

    }

}

@Component
class InvoiceToDbInvoiceConverter(
        val businessObjectDataCopyToDbInvoiceBusinessObjectData: BusinessObjectDataCopyToDbInvoiceBusinessObjectData,
        val metadataToDbMetadataConverter: MetadataToDbMetadataConverter,
        val invoiceRecordsToDbInvoiceRecordsConverter: InvoiceRecordsToDbInvoiceRecordsConverter,
        val invoicePaymentBankAccountToDbInvoicePaymentBankAccountConverter: InvoicePaymentBankAccountToDbInvoicePaymentBankAccountConverter
) : MonoConverter<Invoice, DbInvoice> {
    override fun convert(source: Invoice): Mono<DbInvoice> {

        return Mono
                .zip(
                        invoiceRecordsToDbInvoiceRecordsConverter.convert(source.payload.records),
                        metadataToDbMetadataConverter.convert(source.metadata),
                        businessObjectDataCopyToDbInvoiceBusinessObjectData.convert(source.payload.issuerDataCopy),
                        businessObjectDataCopyToDbInvoiceBusinessObjectData.convert(source.payload.contractorDataCopy))
                .flatMap { t ->
                    if (source.payload.payment.bankAccount != null) {
                        invoicePaymentBankAccountToDbInvoicePaymentBankAccountConverter
                                .convert(source.payload.payment.bankAccount)
                                .map { Tuples.of(t.t1, t.t2, t.t3, t.t4, it) }
                    } else {
                        Mono.just(Tuples.of(t.t1, t.t2, t.t3, t.t4, DbInvoicePaymentBankAccount.empty()))
                    }
                }
                .map {
                    DbInvoice(
                            source.payload.invoiceNumber,
                            it.t1.toSet(),
                            DbPaymentInfo(
                                    source.payload.payment.method.name,
                                    source.payload.payment.dueDate,
                                    if(it.t5.isEmpty()) null else it.t5),
                            source.payload.creationDate,
                            source.payload.saleDate,
                            it.t3,
                            it.t4,
                            source.payload.invoiceNote,
                            it.t2)
                }
    }
}

@Component
class DbInvoiceToInvoiceConverter(
        private val dbInvoiceBusinessObjectDataToBusinessObjectDataConverter: DbInvoiceBusinessObjectDataToBusinessObjectDataConverter,
        private val dbMetadataToMetadataConverter: DbMetadataToMetadataConverter,
        private val dbInvoiceRecordsToInvoiceRecordsConverter: DbInvoiceRecordsToInvoiceRecordsConverter,
        private val dbInvoicePaymentBankAccountToInvoicePaymentBankAccountConverter: DbInvoicePaymentBankAccountToInvoicePaymentBankAccountConverter
) : MonoConverter<DbInvoice, Invoice> {
    override fun convert(source: DbInvoice): Mono<Invoice> {

        return Mono
                .zip(
                        dbInvoiceRecordsToInvoiceRecordsConverter.convert(source.records),
                        dbMetadataToMetadataConverter.convert(source.metadata),
                        dbInvoiceBusinessObjectDataToBusinessObjectDataConverter.convert(source.issuerDataCopy),
                        dbInvoiceBusinessObjectDataToBusinessObjectDataConverter.convert(source.contractorDataCopy))
                .flatMap { t ->
                    if (source.payment.bankAccountCopy != null) {
                        dbInvoicePaymentBankAccountToInvoicePaymentBankAccountConverter
                                .convert(source.payment.bankAccountCopy!!)
                                .map { Tuples.of(t.t1, t.t2, t.t3, t.t4, it) }
                    } else {
                        Mono.just(Tuples.of(t.t1, t.t2, t.t3, t.t4, InvoicePaymentBankAccount.empty()))
                    }
                }
                .map {
                    Invoice(source.id,
                            InvoicePayload(
                                    source.invoiceId,
                                    it.t1,
                                    Payment(PaymentMethod.valueOf(source.payment.peymentMethod),
                                            source.payment.dueDate,
                                            if(it.t5.isEmpty()) null else it.t5),
                                    source.creationDate,
                                    source.saleDate,
                                    it.t3,
                                    it.t4,
                                    source.invoiceNote
                            ),
                            InvoiceMetadata(
                                    source.metadata.ownershipId,
                                    source.metadata.lock,
                                    source.metadata.baseVersion?.toString(),
                                    source.metadata.correctedVersion?.toString())
                    )
                }

    }
}