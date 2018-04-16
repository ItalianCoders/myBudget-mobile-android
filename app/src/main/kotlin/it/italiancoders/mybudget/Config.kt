package it.italiancoders.mybudget

import it.italiancoders.mybudget.manager.LoginResult
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.AccountDetails
import it.italiancoders.mybudget.rest.model.User
import java.util.*
import kotlin.properties.Delegates

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
object Config {

    const val DEFAULT_CATEGORY_EXPENSE_ID = "16"
    const val DEFAULT_CATEGORY_INCOMING_ID = "17"

    interface UserListener {
        fun onChange(oldUser: User?, newUser: User?)
    }

    /**
     * Current token on session
     */
    var accessToken: String? = null

    /**
     * Current refresh token
     */
    var refreshToken: String? = null

    /**
     * Current logged user
     */
    var user by Delegates.observable<User?>(null) { _, old, new ->
        userListener?.onChange(old, new)
    }

    /**
     * Accounts
     */
    var accounts: List<Account>? = null

    /**
     * User locale
     */
    lateinit var locale: Locale

    var userListener: UserListener? = null

    /**
     * Current selected account
     */
    var currentAccount: AccountDetails? = null

    var currentAccountNeedReload: Boolean = false

    fun clear() {
        currentAccount = null
        currentAccountNeedReload = true
        accessToken = null
        refreshToken = null
        user = null
        accounts = null
    }

    fun load(loginResult: LoginResult) {
        refreshToken = loginResult.refreshToken
        accessToken = loginResult.accessToken
        user = loginResult.user
        accounts = loginResult.accounts
        currentAccount = null
        currentAccountNeedReload = true
    }
}