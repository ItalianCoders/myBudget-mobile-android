/*
 * Project: myBudget-mobile-android
 * File: WelcomeFragment.kt
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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.view.FlexibleFrameLayout
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *
 *
 * date: 04/04/18
 */
@EFragment(R.layout.fragment_welcome)
open class WelcomeFragment : BaseFragment(), ViewTreeObserver.OnWindowFocusChangeListener {

    @JvmField
    @ViewById
    internal var loginFragment: FrameLayout? = null

    @JvmField
    @ViewById
    internal var signUpFragment: FrameLayout? = null

    @ViewById
    internal lateinit var wrapper: FlexibleFrameLayout

    @ViewById
    internal lateinit var button: LoginButton

    @ViewById
    internal lateinit var rootLayout: CoordinatorLayout

    private var isLogin = false

    @AfterViews
    internal fun initView() {
        val topLoginFragment = LoginFragment_.builder().build()
        val topSignUpFragment = SignUpFragment_.builder().build()

        activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.loginFragment, topLoginFragment)
                .replace(R.id.signUpFragment, topSignUpFragment)
                .commit()

        signUpFragment!!.rotation = -90f

        button.setOnSignUpListener(topSignUpFragment)
        button.setOnLoginListener(topLoginFragment)

        button.setOnButtonSwitched(object : OnButtonSwitchedListener {
            override fun onButtonSwitched(isLogin: Boolean) {
                rootLayout.setBackgroundColor(ContextCompat.getColor(activity!!, if (isLogin) R.color.welcome_signup else R.color.welcome_login))
            }
        })

        signUpFragment!!.visibility = INVISIBLE

        rootLayout.viewTreeObserver.addOnWindowFocusChangeListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (loginFragment != null && signUpFragment != null) {
            loginFragment!!.pivotX = (loginFragment!!.width / 2).toFloat()
            loginFragment!!.pivotY = loginFragment!!.height.toFloat()
            signUpFragment!!.pivotX = (signUpFragment!!.width / 2).toFloat()
            signUpFragment!!.pivotY = signUpFragment!!.height.toFloat()
        }
    }

    @Click
    internal fun buttonClicked() {
        if (isLogin) {
            loginFragment!!.visibility = VISIBLE
            loginFragment!!.animate().rotation(0f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    signUpFragment!!.visibility = INVISIBLE
                    signUpFragment!!.rotation = 90f
                    wrapper.setDrawOrder(FlexibleFrameLayout.ORDER_LOGIN_STATE)
                }
            })
        } else {
            signUpFragment!!.visibility = VISIBLE
            signUpFragment!!.animate().rotation(0f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    loginFragment!!.visibility = INVISIBLE
                    loginFragment!!.rotation = -90f
                    wrapper.setDrawOrder(FlexibleFrameLayout.ORDER_SIGN_UP_STATE)
                }
            })
        }

        isLogin = !isLogin
        button.startAnimation()
    }

    override fun backPressed(): Boolean {

        MaterialDialog.Builder(activity!!)
                .iconRes(R.mipmap.ic_launcher_round)
                .title(R.string.app_name)
                .content(R.string.app_exit_dialog_content)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ -> activity?.finish() }
                .show()

        return true
    }

    override fun getActionBarTitleResId(): Int {
        return R.string.fragment_title_welcome
    }
}
