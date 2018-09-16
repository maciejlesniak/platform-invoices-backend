package pl.sparkidea.service.invoicespl.services

import org.springframework.stereotype.Service
import pl.sparkidea.service.invoicespl.domain.BusinessObjectData
import pl.sparkidea.service.invoicespl.domain.BusinessRole
import pl.sparkidea.service.invoicespl.repositories.DbBusinessObjectDataRepository
import pl.sparkidea.service.invoicespl.repositories.DbBusinessObjectDataTemplateRepository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbBusinessObjectData
import pl.sparkidea.service.invoicespl.services.converters.BusinessObjectDataToDbBusinessObjectData
import pl.sparkidea.service.invoicespl.services.converters.DbBusinessObjectDataToBusinessObjectDataConverter
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.security.Principal

/**
 *
 * @author Maciej Lesniak
 */
@Service
class BusinessObjectsService(
        private val dbBusinessObjectDataRepository: DbBusinessObjectDataRepository,
        private val businessObjectDataToDbBusinessObjectData: BusinessObjectDataToDbBusinessObjectData,
        private val dbBusinessObjectDataToBusinessObjectDataConverter: DbBusinessObjectDataToBusinessObjectDataConverter,
        private val dbBusinessObjectDataTemplateRepository: DbBusinessObjectDataTemplateRepository
) {

    fun createBusinessObject(obj: BusinessObjectData): Mono<BusinessObjectData> {
        return businessObjectDataToDbBusinessObjectData.convert(obj)
                .flatMap { dbObj: DbBusinessObjectData -> dbBusinessObjectDataRepository.save(dbObj) }
                .flatMap { savedObj: DbBusinessObjectData -> dbBusinessObjectDataToBusinessObjectDataConverter.convert(savedObj) }
    }

    fun getAllContractorsByOwner(principal: Principal): Flux<BusinessObjectData> {
        return dbBusinessObjectDataRepository.findAllByBusinessRoleAndOwnershipId(BusinessRole.CONTRACTOR, principal.name)
                .flatMap { savedObj: DbBusinessObjectData -> dbBusinessObjectDataToBusinessObjectDataConverter.convert(savedObj) }
    }

    fun getAllIssuersByOwner(principal: Principal): Flux<BusinessObjectData> {
        return dbBusinessObjectDataRepository.findAllByBusinessRoleAndOwnershipId(BusinessRole.ISSUER, principal.name)
                .flatMap { savedObj: DbBusinessObjectData -> dbBusinessObjectDataToBusinessObjectDataConverter.convert(savedObj) }
    }

    fun deleteBusinessObject(id: String, principal: Principal): Mono<Long> {
        return dbBusinessObjectDataRepository.deleteAllByIdAndOwnershipId(id, principal.name)
    }

    fun findBusinessObjectByIdAndPrincipal(id: String, principal: Principal): Mono<BusinessObjectData> {
        return dbBusinessObjectDataRepository.findByIdAndOwnershipId(id, principal.name)
                .flatMap { dbBusinessObjectDataToBusinessObjectDataConverter.convert(it) }
    }

    fun updateBusinessObject(id: String, obj: BusinessObjectData, principal: Principal): Mono<BusinessObjectData> {
        return businessObjectDataToDbBusinessObjectData.convert(obj)
                .flatMap { dbBusinessObjectDataTemplateRepository.updateBusinessObject(id, it) }
                .flatMap { savedObj: DbBusinessObjectData -> dbBusinessObjectDataToBusinessObjectDataConverter.convert(savedObj) }
    }
}