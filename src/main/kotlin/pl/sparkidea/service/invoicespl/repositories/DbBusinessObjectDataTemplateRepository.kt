package pl.sparkidea.service.invoicespl.repositories

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import pl.sparkidea.service.invoicespl.repositories.domain.mongo.DbBusinessObjectData
import reactor.core.publisher.Mono

/**
 *
 * @author Maciej Lesniak
 */
@Repository
class DbBusinessObjectDataTemplateRepository(
        private val reactiveMongoTemplate: ReactiveMongoTemplate
) {

    fun updateBusinessObject(objId: String, updatedObj: DbBusinessObjectData):
            Mono<DbBusinessObjectData> = reactiveMongoTemplate
            .findAndModify(
                    Query(Criteria
                            .where("_id")
                            .`is`(objId)
                    ),
                    Update()
                            .set(DbBusinessObjectData.FULL_NAME, updatedObj.objFullName)
                            .set(DbBusinessObjectData.SHORT_NAME, updatedObj.objShortName)
                            .set(DbBusinessObjectData.BANK_ACCOUNT, updatedObj.objIban)
                            .set(DbBusinessObjectData.NIP, updatedObj.objNip)
                            .set(DbBusinessObjectData.REGON, updatedObj.objRegon)
                            .set(DbBusinessObjectData.VAT_PAYER, updatedObj.vatPayer)
                            .set(DbBusinessObjectData.ADDRESS, updatedObj.objAddress)
                            .set(DbBusinessObjectData.BUSINESS_ROLE, updatedObj.businessRole)
                            .set(DbBusinessObjectData.INDIVIDUAL, updatedObj.coordinatorInfoCopy)
                            .set(DbBusinessObjectData.OWNERSHIP, updatedObj.ownershipId)
                            .set(DbBusinessObjectData.TAX_OFFICE, updatedObj.taxOfficeId),
                    DbBusinessObjectData::class.java
            )
}