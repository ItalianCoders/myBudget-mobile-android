package it.italiancoders.mybudget.rest.api

import it.italiancoders.mybudget.rest.model.UserAccountInvite
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
interface PendingInvitesService {

    @GET("/protected/v1/pending-invites")
    fun load(): Call<List<UserAccountInvite>>

    @POST("/protected/v1/pending-invites/{id}/reply")
    fun reply(@Path("id") inviteId: String, @Query("action") action: Int): Call<Void>
}