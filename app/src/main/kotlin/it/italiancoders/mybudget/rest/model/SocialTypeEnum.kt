package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

/**  custom enum
 *
 * Svn revision:1530
 */
enum class SocialTypeEnum constructor(@get:JsonValue val value: Int) : Serializable {

    None(0),

    Facebook(1),

    Google(2);

    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JsonCreator
        fun fromValue(valore: Int): SocialTypeEnum? {
            for (b in SocialTypeEnum.values()) {
                if (b.value == valore) {
                    return b
                }
            }
            return null
        }
    }
}

