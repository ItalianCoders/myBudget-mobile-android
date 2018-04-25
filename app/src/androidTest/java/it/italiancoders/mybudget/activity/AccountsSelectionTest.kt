/*
 * Project: myBudget-mobile-android
 * File: AccountsSelectionTest.kt
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

package it.italiancoders.mybudget.activity


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.fasterxml.jackson.core.type.TypeReference
import it.italiancoders.mybudget.BaseTest
import it.italiancoders.mybudget.JsonTestHelper
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.action.ClickOnView
import it.italiancoders.mybudget.action.RecyclerViewItemCountAssertion
import it.italiancoders.mybudget.matcher.RecyclerViewMatcher
import it.italiancoders.mybudget.rest.model.Account
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AccountsSelectionTest : BaseTest() {

    @Test
    fun accountsCountTest() {

        enqueueResponse(200, "allAccounts.json")
        onView(withId(R.id.accountTV)).perform(click())

        onView(withId(R.id.md_contentRecyclerView)).check(RecyclerViewItemCountAssertion(3))

        pressBack()
    }

    @Test
    fun accountsDataTest() {

        val accounts: List<Account> = JsonTestHelper
                .getJsonObject(InstrumentationRegistry.getInstrumentation().context, "allAccounts.json", object : TypeReference<List<Account>>() {})

        for (orientation in 1..2) {
            enqueueResponse(200, "allAccounts.json")
            onView(withId(R.id.accountTV)).perform(click())

            for (i in 0 until accounts.size) {
                val account = accounts[i]

                onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.textTV))
                        .check(matches(withText(account.name)))

                if (account.numberOfUsers ?: 1 > 1) {
                    onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.membersTV)).check(matches(isDisplayed()))

                    onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.membersTV))
                            .check(matches(withText("${account.numberOfUsers}")))
                } else {
                    viewNotDisplayed(onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.membersTV)))
                }

                if (account.description.isNullOrBlank()) {
                    viewNotDisplayed(onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.descriptionTV)))
                } else {
                    onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.descriptionTV)).check(matches(isDisplayed()))

                    onView(RecyclerViewMatcher(R.id.md_contentRecyclerView).atPositionOnView(i, R.id.descriptionTV))
                            .check(matches(withText(account.description.orEmpty())))
                }
            }

            pressBack()

            rotateLandscape()
        }

        rotatePortrait()
    }

    @Test
    fun accountSelectionTest() {

        val accounts: List<Account> = JsonTestHelper
                .getJsonObject(InstrumentationRegistry.getInstrumentation().context, "allAccounts.json", object : TypeReference<List<Account>>() {})

        for (i in 0 until accounts.size) {
            val account = accounts[i]

            enqueueResponse(200, "allAccounts.json")
            onView(withId(R.id.accountTV)).perform(click())

            enqueueResponse(200, "${account.name.toLowerCase()}AccountDetails.json")
            onView(withId(R.id.md_contentRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ClickOnView(R.id.rootLayout)))

            onView(withId(R.id.accountTV)).check(matches(withText(account.name)))

            if (account.numberOfUsers ?: 1 > 1) {
                onView(withId(R.id.accountMembersImage)).check(matches(isDisplayed()))
                onView(withId(R.id.accountMembersTV)).check(matches(isDisplayed()))
                onView(withId(R.id.accountMembersTV)).check(matches(withText("${account.numberOfUsers}")))
            } else {
                viewNotDisplayed(onView(withId(R.id.accountMembersImage)))
                viewNotDisplayed(onView(withId(R.id.accountMembersTV)))
            }
        }
    }

    private fun viewNotDisplayed(view: ViewInteraction) {
        try {
            view.check(matches(Matchers.not(isDisplayed())))
        } catch (e: Exception) {
            view.check(ViewAssertions.doesNotExist())
        }

    }
}
