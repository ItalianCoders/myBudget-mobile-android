package it.italiancoders.mybudget.rest

import android.content.Context
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.rest.model.Error

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
object ErrorParser {

    fun parse(context: Context, jsonError: String?, titleResId: Int = R.string.error, detailResId: Int = R.string.error_try_later): Error {
        val defaultError = Error()
        defaultError.title = context.resources.getString(titleResId)
        defaultError.detail = context.resources.getString(detailResId)

        jsonError?.let {
            return try {
                val error = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonError, Error::class.java)
                error.title = error.title ?: defaultError.title
                error.detail = error.detail ?: defaultError.detail
                error
            } catch (e: Exception) {
                defaultError
            }
        }
        return defaultError
    }
}