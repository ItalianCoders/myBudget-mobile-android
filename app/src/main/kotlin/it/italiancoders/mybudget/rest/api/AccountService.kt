/*
 * Project: myBudget-mobile-android
 * File: AccountService.kt
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

package it.italiancoders.mybudget.rest.api

import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.AccountCreationRequest
import it.italiancoders.mybudget.rest.model.AccountDetails
import it.italiancoders.mybudget.rest.model.User
import retrofit2.Call
import retrofit2.http.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 25/01/18
 */
interface AccountService {

    @GET("/protected/v1/accounts/{id}")
    fun getAccountDetails(@Path("id") accountId: String): Call<AccountDetails>

    @GET("/protected/v1/accounts")
    fun getAll(): Call<List<Account>>

    @GET("/protected/v1/accounts/{id}/invite/users")
    fun searchUser(@Path("id") accountId: String, @Query("search") searchValue: String): Call<List<User>>

    @POST("/protected/v1/accounts/{id}/invite/users/{username}")
    fun inviteUser(@Path("id") accountId: String, @Path("username") username: String): Call<Void>

    @PUT("/protected/v1/accounts/{id}")
    fun edit(@Path("id") accountId: String, @Body account: Account): Call<Void>

    @POST("/protected/v1/accounts")
    fun insert(@Body account: AccountCreationRequest): Call<Void>

    @DELETE("/protected/v1/accounts/{id}/kick")
    fun kickUser(@Path("id") accountId: String, @Query("username") username: String): Call<Void>

    @DELETE("/protected/v1/accounts/{id}/left")
    fun left(@Path("id") accountId: String): Call<Void>

    @PUT("/protected/v1/accounts/{id}/users/{username}/roles")
    fun changeUserRole(@Path("id") accountId: String,
                       @Path("username") username: String,
                       @Query("role") role: Int): Call<Void>
}