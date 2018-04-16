package it.italiancoders.mybudget.fragment.login

import android.support.v4.app.Fragment
import android.widget.EditText
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.signin.facebook.FacebookLoginActivity_
import it.italiancoders.mybudget.activity.signin.google.GoogleSignInActivity_
import it.italiancoders.mybudget.manager.rest.AuthManager
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.LOGIN_ACTIVITY_REQUEST_CODE
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById


/**
 * @author fattazzo
 *         <p/>
 *         date: 25/03/18
 */
@EFragment(R.layout.fragment_login)
open class LoginFragment : Fragment(), OnLoginListener {

    @ViewById
    internal lateinit var usernameET: EditText

    @ViewById
    internal lateinit var passwordET: EditText

    @Bean
    internal lateinit var authManager: AuthManager

    @Click
    fun googleSignUpButtonClicked() {
        GoogleSignInActivity_.intent(activity).startForResult(LOGIN_ACTIVITY_REQUEST_CODE)
    }

    @Click
    fun facebookSignUpButtonClicked() {
        FacebookLoginActivity_.intent(activity).startForResult(LOGIN_ACTIVITY_REQUEST_CODE)
    }

    override fun login() {
        authManager.login(usernameET.text.toString(), passwordET.text.toString(), SocialTypeEnum.None, "", activity)
    }
}