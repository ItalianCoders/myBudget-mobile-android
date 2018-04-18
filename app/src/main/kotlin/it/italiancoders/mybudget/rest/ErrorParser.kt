package it.italiancoders.mybudget.rest

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import it.italiancoders.mybudget.rest.model.Error

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
object ErrorParser {

    fun parse(jsonError: String?): Error? {
        jsonError?.let {
            return try {
                ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonError, Error::class.java)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
}