package it.italiancoders.mybudget.rest.model

import java.io.Serializable

open class JwtAuthenticationResponse  : Serializable {

    val user: User? = null

    val refreshToken: String? = null

    val accounts: List<Account>? = null
}
