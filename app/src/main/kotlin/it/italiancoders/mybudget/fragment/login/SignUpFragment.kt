/*
 * Project: myBudget-mobile-android
 * File: SignUpFragment.kt
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

import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.widget.Toast
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.rest.AuthManager
import it.italiancoders.mybudget.rest.model.RegistrationUser
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 25/03/18
 */
@EFragment(R.layout.fragment_signup)
open class SignUpFragment : Fragment(), OnSignUpListener {

    @ViewById
    lateinit var usernameTIET: TextInputEditText
    @ViewById
    lateinit var passwordTIET: TextInputEditText
    @ViewById
    lateinit var emailTIET: TextInputEditText
    @ViewById
    lateinit var firstnameTIET: TextInputEditText
    @ViewById
    lateinit var lastnameTIET: TextInputEditText

    @ViewById
    lateinit var usernameLayout: TextInputLayout
    @ViewById
    lateinit var passwordLayout: TextInputLayout
    @ViewById
    lateinit var emailLayout: TextInputLayout
    @ViewById
    lateinit var firstnameLayout: TextInputLayout
    @ViewById
    lateinit var lastnameLayout: TextInputLayout

    @Bean
    lateinit var authManager: AuthManager

    @AfterViews
    fun initView() {
        usernameLayout.markRequired()
        passwordLayout.markRequired()
        emailLayout.markRequired()
        firstnameLayout.markRequired()
        lastnameLayout.markRequired()
    }

    override fun signUp() {
        val registrationUser = RegistrationUser()
        registrationUser.username = usernameTIET.text.toString()
        registrationUser.password = passwordTIET.text.toString()
        registrationUser.email = emailTIET.text.toString()
        registrationUser.firstname = firstnameTIET.text.toString()
        registrationUser.lastname = lastnameTIET.text.toString()

        if (registrationUser.isValid()) {
            authManager.register(registrationUser, activity)
        } else {
            Toast.makeText(activity, resources.getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            usernameTIET.updateError(registrationUser.username.isNullOrBlank(), R.string.required)
            passwordTIET.updateError(registrationUser.password.isNullOrBlank() || registrationUser.password!!.length < 4, R.string.required_min_chars, "4")
            emailTIET.updateError(registrationUser.email.isNullOrBlank(), R.string.required)
            firstnameTIET.updateError(registrationUser.firstname.isNullOrBlank(), R.string.required)
            lastnameTIET.updateError(registrationUser.lastname.isNullOrBlank(), R.string.required)
        }
    }

    private fun TextInputLayout.markRequired() {
        hint = "$hint *"
    }

    private fun TextInputEditText.updateError(show: Boolean, errorResId: Int?, formatArg: Any? = null) {
        error = if (show) {
            getString(errorResId!!, formatArg)
        } else {
            null
        }
    }
}