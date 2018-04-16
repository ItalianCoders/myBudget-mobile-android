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