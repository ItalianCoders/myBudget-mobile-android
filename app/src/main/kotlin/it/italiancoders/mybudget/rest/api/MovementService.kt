package it.italiancoders.mybudget.rest.api

import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.rest.model.Page
import retrofit2.Call
import retrofit2.http.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/04/18
 */
interface MovementService {

    @POST("/protected/v1/accounts/{accountId}/movements")
    fun insert(@Path("accountId") accountId: String,
               @Body movement: Movement): Call<Void>

    @PUT("/protected/v1/accounts/{accountId}/movements/{movementId}")
    fun edit(@Path("accountId") accountId: String,
             @Path("movementId") movementId: String,
             @Body movement: Movement): Call<Void>

    @DELETE("/protected/v1/accounts/{accountId}/movements/{movementId}")
    fun delete(@Path("accountId") accountId: String,
               @Path("movementId") movementId: String): Call<Void>

    @GET("/protected/v1/accounts/{accountId}/movements")
    fun search(@Path("accountId") accountId: String,
               @Query("year") year: Int,
               @Query("month") month: Int,
               @Query("day") day: Int?,
               @Query("user") user: String?,
               @Query("category") category: String?,
               @Query("page") page: Int): Call<Page<Movement>>
}