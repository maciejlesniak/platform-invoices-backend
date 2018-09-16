package pl.sparkidea.service.invoicespl.services.converters

import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import pl.sparkidea.service.invoicespl.domain.InvoiceMetadata
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbInvoiceMetadata
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */

@Component
class MetadataToDbMetadataConverter : MonoConverter<InvoiceMetadata, DbInvoiceMetadata> {
    override fun convert(source: InvoiceMetadata): Mono<DbInvoiceMetadata> {
        return Mono.just(DbInvoiceMetadata(
                source.ownershipId,
                source.lockStatus,
                if (source.baseDocId == null) null else ObjectId(source.baseDocId),
                if (source.correctedDocId == null) null else ObjectId(source.correctedDocId))
        )
    }
}

@Component
class DbMetadataToMetadataConverter : MonoConverter<DbInvoiceMetadata, InvoiceMetadata> {
    override fun convert(source: DbInvoiceMetadata): Mono<InvoiceMetadata> {
        return Mono.just(InvoiceMetadata(
                source.ownershipId,
                source.lock,
                if (source.baseVersion != null) source.baseVersion.toString() else null,
                if (source.correctedVersion != null) source.correctedVersion.toString() else null
        ))
    }
}

