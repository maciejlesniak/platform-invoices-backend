package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Maciej Lesniak
 */
class SequentialNumberRequest(
        @JsonProperty(NUMBER_TYPE) val type: String,
        @JsonProperty(NUMBER_VALUE) val value: Long
) {

    companion object {
        const val NUMBER_TYPE = "type"
        const val NUMBER_VALUE = "val"
    }

}