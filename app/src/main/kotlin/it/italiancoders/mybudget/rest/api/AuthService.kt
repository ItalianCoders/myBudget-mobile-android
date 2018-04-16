package it.italiancoders.mybudget.rest.api

import it.italiancoders.mybudget.rest.model.JwtAuthenticationRequest
import it.italiancoders.mybudget.rest.model.JwtAuthenticationResponse
import it.italiancoders.mybudget.rest.model.RegistrationUser
import it.italiancoders.mybudget.rest.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author fattazzo
 *         <p/>
 *         date: 25/01/18
 */
interface AuthService {

    @POST("/public/v1/login")
    fun login(@Body authRequest: JwtAuthenticationRequest): Call<JwtAuthenticationResponse>

    @POST("/public/v1/register")
    fun register(@Body registrationUser: RegistrationUser): Call<User>
}