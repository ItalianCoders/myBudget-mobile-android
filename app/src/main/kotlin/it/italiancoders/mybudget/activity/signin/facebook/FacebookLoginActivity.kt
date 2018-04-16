package it.italiancoders.mybudget.activity.signin.facebook

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.signin.facebook.callbacks.GetUserCallback
import it.italiancoders.mybudget.activity.signin.facebook.entities.User
import it.italiancoders.mybudget.activity.signin.facebook.requests.UserRequest
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_ID
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_PWD
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_TOKEN
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_TYPE
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_LOGIN
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_LOGOUT
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById
import java.util.*


@EActivity(R.layout.activity_signin_facebook)
open class FacebookLoginActivity : AppCompatActivity(), GetUserCallback.IGetUserResponse {

    @ViewById
    lateinit var loginButton: LoginButton

    @ViewById
    lateinit var accountView: FacebookSignInAccountView

    @ViewById
    lateinit var statusView: TextView

    private var mCallbackManager: CallbackManager? = null

    private var accessTokenTracker: AccessTokenTracker? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    @AfterViews
    protected fun initView() {

        mCallbackManager = CallbackManager.Factory.create()

        // Set the initial permissions to request from the user while logging in
        loginButton.setReadPermissions(Arrays.asList(EMAIL))

        // Register a callback to respond to the user
        loginButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                UserRequest.makeUserRequest(GetUserCallback(this@FacebookLoginActivity).callback)
                //val id = Profile.getCurrentProfile().getId()

                val returnIntent = Intent()
                returnIntent.putExtra(EXTRA_LOGIN_TOKEN, loginResult.accessToken.token)
                returnIntent.putExtra(EXTRA_LOGIN_ID, loginResult.accessToken.userId)
                returnIntent.putExtra(EXTRA_LOGIN_PWD, "*")
                returnIntent.putExtra(EXTRA_LOGIN_TYPE, SocialTypeEnum.Facebook.value)
                setResult(RESULT_ACC_LOGIN, returnIntent)
                finish()
            }

            override fun onCancel() {
                onCompleted(null)
                setResult(Activity.RESULT_CANCELED)
                finish()
            }

            override fun onError(e: FacebookException) {
                onCompleted(null)
                statusView.setText(R.string.sign_in_error)
            }
        })

        UserRequest.makeUserRequest(GetUserCallback(this@FacebookLoginActivity).callback)

        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(accessToken: AccessToken?, accessToken2: AccessToken?) {
                if (accessToken2 == null) {
                    onCompleted(null)
                    setResult(RESULT_ACC_LOGOUT)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        accessTokenTracker?.stopTracking()
        super.onDestroy()
    }

    override fun onCompleted(user: User?) {
        accountView.bind(user)

        if (user != null) {
            statusView.setText(R.string.signed_in)
        } else {
            statusView.setText(R.string.signed_out)
        }
    }

    companion object {

        private const val EMAIL = "email"
    }
}