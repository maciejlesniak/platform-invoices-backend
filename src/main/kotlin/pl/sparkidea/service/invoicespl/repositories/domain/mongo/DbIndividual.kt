package pl.sparkidea.service.invoicespl.repositories.domain.mongo

import org.springframework.data.mongodb.core.mapping.Field

/**
 *
 * @author Maciej Lesniak
 */
class DbIndividual(
        @Field(FIRST_NAME) val firstName: String,
        @Field(SECOND_NAME) val secondName: String,
        @Field(LAST_NAME) val lastName: String
) {
    companion object {
        const val FIRST_NAME = "first_name"
        const val SECOND_NAME = "second_name"
        const val LAST_NAME = "last_name"
    }
}