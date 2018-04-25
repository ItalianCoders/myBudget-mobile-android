/*
 * Project: myBudget-mobile-android
 * File: LoginFragment.kt
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