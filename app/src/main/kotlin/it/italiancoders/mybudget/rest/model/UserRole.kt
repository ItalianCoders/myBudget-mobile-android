package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

/**
 * @author fattazzo
 *         <p/>
 *         date: 15/04/18
 */
enum class UserRole(@JsonValue val value: Int) : Serializable {

    Standard(0), Admin(1);

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JsonCreator
        fun fromValue(text: String): UserRole? {
            for (b in UserRole.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}