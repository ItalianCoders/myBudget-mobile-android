package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue


/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
enum class InviteStatus(@JsonValue val value: Int) {

    Confirmed(0), Declined(1);


    override fun toString(): String {
        return value.toString()
    }

    companion object {

        @JsonCreator
        fun fromValue(text: String): InviteStatus? {
            for (b in InviteStatus.values()) {
                if (b.value.toString() == text) {
                    return b
                }
            }
            return null
        }
    }
}