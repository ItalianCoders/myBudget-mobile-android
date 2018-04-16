package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


open class Movement : Serializable {

    @JsonProperty("id")
    val id: String? = null

    @JsonProperty("type")
    var type: MovementType? = null

    @JsonProperty("amount")
    var amount: Double? = null

    @JsonProperty("executedBy")
    var executedBy: User? = null

    @JsonProperty("executedAt")
    var executedAt: Long? = null

    @JsonProperty("uptadedAt")
    var uptadedAt: Long? = null

    @JsonProperty("note")
    var note: String? = null

    @JsonProperty("category")
    var category: Category? = null

    @JsonIgnore
    val account: Account? = null

    @JsonIgnore
    @JsonProperty("isAuto")
    val auto = false
}