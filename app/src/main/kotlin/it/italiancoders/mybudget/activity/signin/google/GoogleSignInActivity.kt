/*
 * Project: myBudget-mobile-android
 * File: GoogleSignInActivity.kt
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

package it.italiancoders.mybudget.activity.signin.google

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.rest.AuthManager
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_ID
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_TOKEN
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_TYPE
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_LOGOUT
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_REMOVE
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@SuppressLint("RestrictedApi")
@EActivity(R.layout.activity_signin_google)
open class GoogleSignInActivity : AppCompatActivity() {

    @ViewById
    lateinit var statusView: TextView

    @ViewById
    lateinit var signInButton: SignInButton

    @ViewById
    lateinit var signOutAndDisconnectLayout: ConstraintLayout

    @ViewById
    lateinit var accountView: GoogleSignInAccountView

    private var mGoogleSignInClient: GoogleSignInClient? = null

    @AfterViews
    protected fun initView() {
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.google_client_id))
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    public override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account, false)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account, false)
            val returnIntent = Intent()
            returnIntent.putExtra(EXTRA_LOGIN_TOKEN, account.idToken)
            returnIntent.putExtra(EXTRA_LOGIN_ID, account.email)
            returnIntent.putExtra(AuthManager.EXTRA_LOGIN_PWD, "*")
            returnIntent.putExtra(EXTRA_LOGIN_TYPE, SocialTypeEnum.Google.value)
            setResult(AuthManager.RESULT_ACC_LOGIN, returnIntent)
            finish()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null, true)
        }

    }

    @Click
    protected fun signInButtonClicked() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Click
    protected fun signOutButtonClicked() {
        mGoogleSignInClient!!.signOut()
                .addOnCompleteListener(this) {
                    updateUI(null, false)
                    setResult(RESULT_ACC_LOGOUT)
                    finish()
                }
    }

    @Click
    protected fun disconnectButtonClicked() {
        mGoogleSignInClient!!.revokeAccess()
                .addOnCompleteListener(this) {
                    updateUI(null, false)
                    setResult(RESULT_ACC_REMOVE)
                    finish()
                }
    }

    private fun updateUI(account: GoogleSignInAccount?, hasError: Boolean) {
        if (account != null) {
            statusView.setText(R.string.signed_in)
            signInButton.visibility = View.GONE
            signOutAndDisconnectLayout.visibility = View.VISIBLE
        } else {
            statusView.setText(R.string.signed_out)
            signInButton.visibility = View.VISIBLE
            signOutAndDisconnectLayout.visibility = View.GONE
        }
        accountView.bind(account)

        if (hasError) {
            statusView.setText(R.string.sign_in_error)
        }
    }

    companion object {

        private const val TAG = "GoogleSignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}
