/*
 * Project: myBudget-mobile-android
 * File: RestClient.kt
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

package it.italiancoders.mybudget.rest

import it.italiancoders.mybudget.rest.api.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
object RestClient {

    /**
     * Base url of REST api
     */
    var BASE_URL = "https://floating-ravine-25522.herokuapp.com/"

    val authService: AuthService
        get() = RetrofitClient.getClient(BASE_URL).create(AuthService::class.java)

    val accountService: AccountService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(AccountService::class.java)

    val movementService: MovementService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(MovementService::class.java)

    val pendingInvitesService: PendingInvitesService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(PendingInvitesService::class.java)

    val scheduledMovementService: ScheduledMovementService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(ScheduledMovementService::class.java)

    val budgetService: BudgetService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(BudgetService::class.java)
}