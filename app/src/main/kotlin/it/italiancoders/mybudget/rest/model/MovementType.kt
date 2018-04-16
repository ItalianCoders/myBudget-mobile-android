package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

/**  custom enum
 *
 * Svn revision:1530
 */
enum class MovementType constructor(@get:JsonValue val value: Int?) : Serializable {

    Incoming(0), Expense(1), Both(2);

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JsonCreator
        fun fromValue(text: String): MovementType? {
            for (b in MovementType.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}