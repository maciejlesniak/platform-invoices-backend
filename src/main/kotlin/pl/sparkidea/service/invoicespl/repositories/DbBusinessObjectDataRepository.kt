package pl.sparkidea.service.invoicespl.repositories

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import pl.sparkidea.service.invoicespl.domain.BusinessRole
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbBusinessObjectData
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
interface DbBusinessObjectDataRepository : ReactiveCrudRepository<DbBusinessObjectData, String> {

    fun findAllByBusinessRoleAndOwnershipId(businessRole: BusinessRole, ownershipId: String): Flux<DbBusinessObjectData>

    fun findByIdAndOwnershipId(id: String, s: String): Mono<DbBusinessObjectData>

    fun deleteAllByIdAndOwnershipId(id: String, s: String): Mono<Long>

}