package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

/**  custom enum
 *
 * Svn revision:1530
 */
enum class AccountStatusEnum(@get:JsonValue val value: Int?) : Serializable {

    Ok(0), Warning(1), Critical(2);

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JsonCreator
        fun fromValue(text: String): AccountStatusEnum? {
            for (b in AccountStatusEnum.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }


}