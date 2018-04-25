/*
 * Project: myBudget-mobile-android
 * File: Config.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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