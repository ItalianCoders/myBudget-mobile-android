package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
open class UserAccountInvite : Serializable {

    @JsonProperty("id")
    lateinit var id: String

    @JsonProperty("user")
    lateinit var user: User

    @JsonProperty("invitedBy")
    lateinit var invitedBy: User

    @JsonProperty("accountId")
    val accountId: String? = null

    @JsonProperty("createdAt")
    var createdAt: Long? = null

    @JsonProperty("updatedAt")
    var updatedAt: Long? = null
}