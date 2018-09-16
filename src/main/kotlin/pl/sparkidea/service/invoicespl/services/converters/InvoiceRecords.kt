package pl.sparkidea.service.invoicespl.services.converters

import org.bson.types.Decimal128
import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.InvoiceRecord
import pl.sparkidea.service.invoicespl.dto.InvoiceRecordRequest
import pl.sparkidea.service.invoicespl.dto.InvoiceRecordResponse
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoiceRecord
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.*
import java.util.stream.Collectors

/**
 *
 * @author Maciej Lesniak
 */
@Component
class InvoiceRecordsRequestToInvoiceRecordsConverter : MonoConverter<Collection<InvoiceRecordRequest>, Collection<InvoiceRecord>> {
    override fun convert(source: Collection<InvoiceRecordRequest>): Mono<Collection<InvoiceRecord>> {

        return Flux.fromIterable(source)
                .map {
                    InvoiceRecord(
                            it.subject,
                            it.unitName,
                            BigDecimal.valueOf(it.qty.toDouble()),
                            BigDecimal.valueOf(it.unitPrice.toDouble()),
                            BigDecimal.valueOf(it.taxBid.toDouble()),
                            Currency.getInstance(it.currencyCode)
                    )
                }
                .collect(Collectors.toList())

    }
}

@Component
class InvoiceRecordsToInvoiceRecordInfosResponseConverter : MonoConverter<Collection<InvoiceRecord>, Collection<InvoiceRecordResponse>> {
    override fun convert(source: Collection<InvoiceRecord>): Mono<Collection<InvoiceRecordResponse>> {

        return Flux.fromIterable(source)
                .map {
                    InvoiceRecordResponse(
                            it.subject,
                            it.quantity.toFloat(),
                            it.nettUnitValue.toFloat(),
                            it.measureUnits,
                            it.taxBidPrecentage.toFloat(),
                            it.currency.currencyCode
                    )
                }
                .collect(Collectors.toList())

    }
}

@Component
class InvoiceRecordsToDbInvoiceRecordsConverter : MonoConverter<Collection<InvoiceRecord>, Collection<DbInvoiceRecord>> {
    override fun convert(source: Collection<InvoiceRecord>): Mono<Collection<DbInvoiceRecord>> {

        return Flux.fromIterable(source)
                .map {
                    DbInvoiceRecord(
                            it.subject,
                            Decimal128(it.quantity),
                            Decimal128(it.nettUnitValue),
                            Decimal128(it.taxBidPrecentage),
                            it.measureUnits,
                            it.currency.currencyCode
                    )
                }
                .collect(Collectors.toList())
    }
}


@Component
class DbInvoiceRecordsToInvoiceRecordsConverter : MonoConverter<Collection<DbInvoiceRecord>, Collection<InvoiceRecord>> {
    override fun convert(source: Collection<DbInvoiceRecord>): Mono<Collection<InvoiceRecord>> {

        return Flux.fromIterable(source)
                .map {
                    InvoiceRecord(
                            it.subject,
                            it.measureUnits,
                            it.quantity.bigDecimalValue(),
                            it.nettUnitValue.bigDecimalValue(),
                            it.taxBidPrecentage.bigDecimalValue(),
                            Currency.getInstance(it.currencyISO4217Symbol)
                    )
                }
                .collect(Collectors.toList())
    }
}

