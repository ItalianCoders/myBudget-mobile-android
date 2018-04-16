package it.italiancoders.mybudget.rest

import it.italiancoders.mybudget.rest.api.AccountService
import it.italiancoders.mybudget.rest.api.AuthService
import it.italiancoders.mybudget.rest.api.MovementService
import it.italiancoders.mybudget.rest.api.PendingInvitesService

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
object RestClient {

    /**
     * Base url of REST api
     */
    private const val BASE_URL = "https://floating-ravine-25522.herokuapp.com/"

    val authService: AuthService
        get() = RetrofitClient.getClient(BASE_URL).create(AuthService::class.java)

    val accountService: AccountService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(AccountService::class.java)

    val movementService: MovementService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(MovementService::class.java)

    val pendingInvitesService: PendingInvitesService
        get() = RetrofitClient.getSecureClient(BASE_URL).create(PendingInvitesService::class.java)
}