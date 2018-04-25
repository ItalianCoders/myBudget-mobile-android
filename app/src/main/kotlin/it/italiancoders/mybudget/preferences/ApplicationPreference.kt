/*
 * Project: myBudget-mobile-android
 * File: ApplicationPreference.kt
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

package it.italiancoders.mybudget.preferences

import org.androidannotations.annotations.sharedpreferences.DefaultInt
import org.androidannotations.annotations.sharedpreferences.DefaultString
import org.androidannotations.annotations.sharedpreferences.SharedPref

/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
interface ApplicationPreference {

    /**
     * App language
     */
    fun language(): String?

    /**
     * App currency
     */
    @DefaultString("EUR")
    fun currency(): String?

    /**
     * Chart color theme
     */
    @DefaultString("4")
    fun chartColorTheme(): String

    // ------------------------------------------------------------------------------------

    /**
     * Type of user account
     *
     * @return -1 if no user account configurated
     */
    @DefaultInt(-1)
    fun accountType(): Int

    /**
     * Last access token
     */
    @DefaultString("")
    fun lastAccessToken(): String?

    /**
     * Last refresh token
     */
    @DefaultString("")
    fun lastRefreshToken(): String?

    /**
     * Last user
     */
    @DefaultString("")
    fun lastUser(): String?

    /**
     * Last user accounts
     */
    @DefaultString("")
    fun lastUserAccounts(): String?

    /**
     * Last chart type selected
     */
    @DefaultInt(1)
    fun lastChartType(): Int
}