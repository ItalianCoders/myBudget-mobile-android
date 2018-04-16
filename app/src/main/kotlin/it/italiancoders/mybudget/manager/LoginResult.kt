package it.italiancoders.mybudget.manager

import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import it.italiancoders.mybudget.rest.model.User

/**
 * @author fattazzo
 *         <p/>
 *         date: 12/04/18
 */
enum class LoginResult {

    Valid, Invalid, Error;

    var refreshToken: String? = null
    var accessToken: String? = null
    var user: User? = null
    var accounts: List<Account>? = null
    lateinit var socialAuthenticationType: SocialTypeEnum

}