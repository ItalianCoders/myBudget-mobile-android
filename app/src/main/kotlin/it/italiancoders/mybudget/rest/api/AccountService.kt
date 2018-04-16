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
}