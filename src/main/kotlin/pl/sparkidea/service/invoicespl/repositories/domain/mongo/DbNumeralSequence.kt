package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

/**
 *
 * @author Maciej Lesniak
 */
@Document(collection = "pl_sequences")
@CompoundIndex(
        name =  "NameAndOwnershipId",
        unique = true,
        def =   "{'${DbNumeralSequence.NAME}': 1, " +
                "'${DbNumeralSequence.OWNERSHIP_ID}': 1, " +
                "'${DbNumeralSequence.BUSINESS_UNIT_ID}': 1}"
)
class DbNumeralSequence(
        @Field(NAME) var name: String,
        @Field(VALUE) var value: Long,
        @Field(OWNERSHIP_ID) val ownershipId: String,
        @Field(BUSINESS_UNIT_ID) val businessUnitId: ObjectId) {

    companion object {
        const val NAME = "name"
        const val VALUE = "val"
        const val OWNERSHIP_ID = "ownership_id"
        const val BUSINESS_UNIT_ID = "bu_id"
    }

    @Id
    lateinit var id: String

    @Version
    var version: Long = -1

}