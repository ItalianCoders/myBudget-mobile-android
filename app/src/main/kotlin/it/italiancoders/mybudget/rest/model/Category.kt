package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class Category : Serializable {

    val type: MovementType? = null

    val id: String? = null

    var value: String? = null

    var iconId: Int? = null

    @JsonProperty("isEditable")
    val editable: Boolean? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}