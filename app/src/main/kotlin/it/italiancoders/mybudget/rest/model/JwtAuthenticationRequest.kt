package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class JwtAuthenticationRequest()  : Serializable {

    @JsonProperty("username")
    var username: String? = null
        private set

    @JsonProperty("password")
    var password: String? = null
        private set

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("socialAuthenticationType")
    var socialAuthenticationType: SocialTypeEnum? = null
        private set

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("socialAccessToken")
    var socialAccessToken: String? = null
        private set

    constructor(username: String, password: String, socialAuthenticationType: SocialTypeEnum, socialAccessToken: String) : this() {
        this.username = username
        this.password = password
        this.socialAuthenticationType = socialAuthenticationType
        this.socialAccessToken = socialAccessToken
    }
}
