package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

/**  custom enum
 *
 * Svn revision:1530
 */
enum class GenderEnum(@get:JsonValue val value: Int?) : Serializable {

    Male(0), Female(1), Others(2);

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JsonCreator
        fun fromValue(text: String): GenderEnum? {
            for (b in GenderEnum.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}

