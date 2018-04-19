package it.italiancoders.mybudget.manager.rest

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.facebook.AccessToken
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.android.gms.auth.api.signin.GoogleSignIn
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.signin.facebook.FacebookLoginActivity_
import it.italiancoders.mybudget.activity.signin.google.GoogleSignInActivity_
import it.italiancoders.mybudget.activity.signin.standard.StandardLoginActivity_
import it.italiancoders.mybudget.fragment.login.WelcomeFragment_
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.manager.LoginResult
import it.italiancoders.mybudget.preferences.ApplicationPreferenceManager
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.JwtAuthenticationRequest
import it.italiancoders.mybudget.rest.model.RegistrationUser
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.Background
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.UiThread
import java.lang.Exception


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class AuthManager {

    @Bean
    lateinit var preferenceManager: ApplicationPreferenceManager

    private var dialog: MaterialDialog? = null

    enum class RegistrationResult { Success, Error;

        var user: User? = null
        var description: String? = null
    }

    open fun logout(activity: Activity?) {
        // per ora uguale a remove finchè non vengono implementate le rest api
        remove(activity)
    }

    open fun remove(activity: Activity?) {
        Config.clear()
        preferenceManager.clearUserInformation()

        FragmentUtils.replace(activity, WelcomeFragment_.builder().build())
    }

    open fun login(socialAuthenticationType: SocialTypeEnum?, activity: Activity?) {
        when (socialAuthenticationType) {
            SocialTypeEnum.Facebook -> {
                val accessToken = AccessToken.getCurrentAccessToken()
                if (accessToken == null) {
                    FacebookLoginActivity_.intent(activity).startForResult(LOGIN_ACTIVITY_REQUEST_CODE)
                } else {
                    login(accessToken.userId, SOCIAL_DEFAULT_PWD, SocialTypeEnum.Facebook, accessToken.token, activity)
                }
            }
            SocialTypeEnum.Google -> {
                val account = GoogleSignIn.getLastSignedInAccount(activity)
                if (account == null) {
                    GoogleSignInActivity_.intent(activity).startForResult(LOGIN_ACTIVITY_REQUEST_CODE)
                } else {
                    login(account.email.orEmpty(), SOCIAL_DEFAULT_PWD, SocialTypeEnum.Google, account.idToken.orEmpty(), activity)
                }
            }
            SocialTypeEnum.None -> StandardLoginActivity_.intent(activity).startForResult(LOGIN_ACTIVITY_REQUEST_CODE)
            else -> {
                // no type
            }
        }
    }

    @Background
    open fun login(username: String, password: String, socialAuthenticationType: SocialTypeEnum, socialAccessToken: String, activity: Activity?) {
        openIndeterminateDialog(R.string.login_in_progress, activity!!)
        var loginResult = LoginResult.Error
        try {
            loginResult = doLogin(username, password, socialAuthenticationType, socialAccessToken)
        } finally {
            closeIndeterminateDialog()
            loginComplete(loginResult, activity)
        }
    }

    private fun doLogin(username: String, password: String, socialAuthenticationType: SocialTypeEnum, socialAccessToken: String): LoginResult {

        val authRequest = JwtAuthenticationRequest(username, password, socialAuthenticationType, socialAccessToken)

        val response = RestClient.authService.login(authRequest).execute()

        return try {
            if (response.isSuccessful) {
                val loginResult = LoginResult.Valid
                loginResult.refreshToken = response.body()!!.refreshToken
                loginResult.accessToken = response.headers().get("X-Auth-Token")
                loginResult.user = response.body()!!.user
                loginResult.accounts = response.body()!!.accounts
                loginResult.socialAuthenticationType = socialAuthenticationType
                loginResult
            } else {
                LoginResult.Invalid
            }
        } catch (e: Exception) {
            LoginResult.Error
        }
    }

    @UiThread
    protected open fun loginComplete(loginResult: LoginResult, activity: Activity?) {
        when (loginResult) {
            LoginResult.Valid -> {
                Config.load(loginResult)
                preferenceManager.storeUserInformation(loginResult)

                Toast.makeText(activity, activity!!.resources.getText(R.string.login_successful), Toast.LENGTH_SHORT).show()
                FragmentUtils.replace(activity, MainFragment_.builder().build())
            }
            LoginResult.Invalid -> Toast.makeText(activity, activity!!.resources.getString(R.string.login_invalid_credential), Toast.LENGTH_SHORT).show()
            LoginResult.Error -> Toast.makeText(activity, activity!!.resources.getString(R.string.login_error), Toast.LENGTH_SHORT).show()
        }
    }

    @Background
    open fun register(registrationUser: RegistrationUser, activity: Activity?) {
        openIndeterminateDialog(R.string.registration_in_progress, activity!!)
        var registrationResult = RegistrationResult.Error
        try {
            val response = RestClient.authService.register(registrationUser).execute()

            try {
                if (response.isSuccessful) {
                    registrationResult = RegistrationResult.Success
                    registrationResult.user = response.body()
                    registrationResult.user!!.password = registrationUser.password
                } else {
                    val jsonError = response.errorBody()?.string().orEmpty()
                    val node = ObjectMapper().readValue(jsonError, ObjectNode::class.java)
                    if (node.has("detail")) {
                        registrationResult.description = node.get("detail").toString()
                    }
                }
            } catch (e: Exception) {
                registrationResult.description = activity.resources.getString(R.string.registration_error)
            }
        } finally {
            closeIndeterminateDialog()
            registrationComplete(registrationResult, activity)
        }
    }

    @UiThread
    protected open fun registrationComplete(registrationResult: RegistrationResult, activity: Activity?) {
        when (registrationResult) {
            RegistrationResult.Success -> {
                login(registrationResult.user?.username.orEmpty(),
                        registrationResult.user?.password.orEmpty(),
                        SocialTypeEnum.None,
                        "",
                        activity)
            }
            RegistrationResult.Error -> Toast.makeText(activity, registrationResult.description, Toast.LENGTH_SHORT).show()
        }
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun openIndeterminateDialog(titleResId: Int, context: Context) {
        if (dialog == null || !dialog!!.isShowing) {
            dialog = MaterialDialog.Builder(context)
                    .title(titleResId)
                    .content(R.string.dialog_wait_content)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .build()
            dialog!!.show()
        }
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun closeIndeterminateDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    companion object {

        const val LOGIN_ACTIVITY_REQUEST_CODE = 2000
        const val RESULT_ACC_LOGIN = 2001
        const val RESULT_ACC_LOGOUT = 2002
        const val RESULT_ACC_REMOVE = 2003

        const val EXTRA_LOGIN_TOKEN = "token"
        const val EXTRA_LOGIN_ID = "id"
        const val EXTRA_LOGIN_TYPE = "type"
        const val EXTRA_LOGIN_PWD = "password"

        const val SOCIAL_DEFAULT_PWD = "*"
    }
}