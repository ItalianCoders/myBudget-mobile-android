package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


open class Account : Serializable {

    @JsonProperty("id")
    lateinit var id: String

    lateinit var name: String

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("description")
    var description: String? = null

    @JsonProperty("status")
    val status: AccountStatusEnum? = null

    @JsonProperty("numberOfUsers")
    val numberOfUsers: Int? = null

    @JsonIgnore
    val defaultUsername: String? = null
}