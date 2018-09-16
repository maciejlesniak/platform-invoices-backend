package pl.sparkidea.service.invoicespl.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @author Maciej Lesniak
 */
class InvoiceNumberResponse(
        @JsonProperty(NUMBER_VALUE) val number: String
) {

    companion object {
        const val NUMBER_VALUE = "number"
    }

}