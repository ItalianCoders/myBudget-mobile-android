package it.italiancoders.mybudget.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.view.View

import org.hamcrest.Matcher

class ClickOnView(private var textViewId: Int) : ViewAction {

    private var click = ViewActions.click()

    override fun getConstraints(): Matcher<View> {
        return click.constraints

    }

    override fun getDescription(): String {
        return " click on View with id: $textViewId"
    }

    override fun perform(uiController: UiController, view: View) {
        click.perform(uiController, view.findViewById(textViewId))
    }
}