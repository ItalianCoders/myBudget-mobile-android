package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import java.io.Serializable


@JsonRootName(value = "user")
class User : Serializable {

    @JsonProperty("username")
    lateinit var username: String

    @JsonProperty(value = "password")
    var password: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("email")
    val email: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("firstname")
    val firstname: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("lastname")
    val lastname: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("alias")
    var alias: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("gender")
    val gender: GenderEnum? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("socialType")
    val socialType: SocialTypeEnum? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("profileImageUrl")
    val profileImageUrl: String? = null

    override fun toString(): String {
        return "User(username='$username', password=$password, email=$email, firstname=$firstname, lastname=$lastname, alias=$alias, gender=$gender, socialType=$socialType)"
    }


}
