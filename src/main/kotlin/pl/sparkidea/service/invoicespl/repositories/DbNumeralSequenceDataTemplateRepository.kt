package pl.sparkidea.service.invoicespl.repositories

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbNumeralSequence
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
@Repository
class DbNumeralSequenceDataTemplateRepository(
        private val reactiveMongoTemplate: ReactiveMongoTemplate
) {
    companion object {
        fun prepareCriteria(name: String, ownershipId: String, businessUnitId: String): CriteriaDefinition = Criteria
                .where(DbNumeralSequence.NAME).`is`(name)
                .and(DbNumeralSequence.OWNERSHIP_ID).`is`(ownershipId)
                .and(DbNumeralSequence.BUSINESS_UNIT_ID).`is`(ObjectId(businessUnitId))
    }


    fun getValueOrInitIfNotFound(name: String, businessUnitId: String, ownershipId: String): Mono<Long?> {
        return reactiveMongoTemplate.findOne<DbNumeralSequence>(
                Query(prepareCriteria(name, ownershipId, businessUnitId)),
                DbNumeralSequence::class.java)
                .switchIfEmpty(upsertSequence(
                        name,
                        businessUnitId,
                        ownershipId,
                        0L)
                        .map { DbNumeralSequence(name, it, ownershipId, ObjectId(businessUnitId)) }
                )
                .map { it.value }
    }

    fun upsertSequence(name: String, businessUnitId: String, ownershipId: String, newValue: Long): Mono<Long> {
        return reactiveMongoTemplate.upsert(
                Query(prepareCriteria(name, ownershipId, businessUnitId)),
                Update.update(DbNumeralSequence.VALUE, newValue),
                DbNumeralSequence::class.java)
                .flatMap {
                    return@flatMap when {
                        it.modifiedCount > 0 -> Mono.just(newValue)
                        it.upsertedId.toString().isNotEmpty() -> Mono.just(newValue)
                        else -> Mono.error(Throwable("not updated due to error"))
                    }
                }
    }

}