/*
 * Project: myBudget-mobile-android
 * File: Matcher.kt
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

package it.italiancoders.mybudget.matcher

import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.internal.util.Checks
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 * @author fattazzo
 *
 *
 * date: 20/04/18
 */
object Matcher {

    fun childAtPosition(
            parentMatcher: org.hamcrest.Matcher<View>, position: Int): org.hamcrest.Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position))
            }
        }
    }

    fun withChildViewCount(count: Int, childMatcher: Matcher<View>?): Matcher<View> {
        return object : BoundedMatcher<View, ViewGroup>(ViewGroup::class.java) {
            override fun matchesSafely(viewGroup: ViewGroup): Boolean {
                var matchCount = 0
                if (childMatcher == null) {
                    matchCount = viewGroup.childCount
                } else {
                    for (i in 0 until viewGroup.childCount) {
                        if (childMatcher.matches(viewGroup.getChildAt(i))) {
                            matchCount++
                        }
                    }
                }

                return matchCount == count
            }

            override fun describeTo(description: Description) {
                description.appendText("ViewGroup with child-count=$count")
                if (childMatcher != null) {
                    description.appendText(" and ")
                    childMatcher.describeTo(description)
                }
            }
        }
    }

    fun withTextViewTextColor(color: Int): Matcher<View> {
        Checks.checkNotNull(color)
        return object : BoundedMatcher<View, TextView>(TextView::class.java) {
            public override fun matchesSafely(textView: TextView): Boolean {
                return ContextCompat.getColor(getTargetContext(),color) == textView.currentTextColor
            }

            override fun describeTo(description: Description) {
                description.appendText("with text color: ")
            }
        }
    }
}
