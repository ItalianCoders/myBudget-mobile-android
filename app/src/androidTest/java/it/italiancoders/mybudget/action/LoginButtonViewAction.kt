package it.italiancoders.mybudget.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
import it.italiancoders.mybudget.fragment.login.LoginButton
import org.hamcrest.Matcher

/**
 * @author fattazzo
 *         <p/>
 *         date: 10/04/18
 */
class LoginButtonViewAction : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(LoginButton::class.java)
    }


    override fun getDescription(): String {
        return "whatever"
    }

    override fun perform(uiController: UiController, view: View) {
        val yourCustomView = view as LoginButton
        yourCustomView.getOnLoginListener()!!.login()
    }

}