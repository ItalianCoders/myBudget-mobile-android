package it.italiancoders.mybudget.activity.signin.standard

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.rest.AuthManager
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
@EActivity(R.layout.activity_login_standard)
open class StandardLoginActivity : AppCompatActivity() {

    @ViewById
    lateinit var usernameET: EditText

    @ViewById
    lateinit var passwordET: EditText

    @Click
    fun appUserLoginButtonClicked() {
        val returnIntent = Intent()
        returnIntent.putExtra(AuthManager.EXTRA_LOGIN_PWD, passwordET.text.toString())
        returnIntent.putExtra(AuthManager.EXTRA_LOGIN_ID, usernameET.text.toString())
        returnIntent.putExtra(AuthManager.EXTRA_LOGIN_TYPE, SocialTypeEnum.None.value)
        setResult(AuthManager.RESULT_ACC_LOGIN, returnIntent)
        finish()
    }
}