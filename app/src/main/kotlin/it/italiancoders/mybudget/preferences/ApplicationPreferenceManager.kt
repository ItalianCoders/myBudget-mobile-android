package it.italiancoders.mybudget.preferences

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.mynameismidori.currencypicker.ExtendedCurrency
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.manager.LoginResult
import it.italiancoders.mybudget.preferences.chart.ChartColorTheme
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.User
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.sharedpreferences.Pref
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class ApplicationPreferenceManager {

    @Pref
    internal lateinit var prefs: ApplicationPreference_

    /**
     * @return application language
     */
    fun getAppLocale(): Locale {
        val language = prefs.language().getOr(Locale.getDefault().language)
        return Locale(language!!.toLowerCase())
    }

    /**
     * App currency
     */
    fun getCurrency(): ExtendedCurrency {
        val currencyCode = prefs.currency().get()

        return ExtendedCurrency.getCurrencyByISO(currencyCode)
                ?: ExtendedCurrency.getCurrencyByISO("EUR")
    }

    /**
     * Last chart type selected
     */
    fun getChartType(): Int {
        return prefs.lastChartType().get()
    }

    /**
     * Chart color theme
     */
    val chartColorTheme: ChartColorTheme
        get() {
            val idxPrefs = prefs.chartColorTheme().get()

            val theme: ChartColorTheme
            theme = try {
                val idx = Integer.parseInt(idxPrefs)
                ChartColorTheme.values()[idx]
            } catch (e: Exception) {
                ChartColorTheme.VORDIPLOM
            }

            return theme
        }

    // ------------------------------------------------------------------------------

    /**
     * Type of user account
     *
     * @return -1 if no user account configurated
     */
    fun getAccountType(): Int = prefs.accountType().get()

    /**
     * Last access token
     */
    fun getLastAccessToken(): String = prefs.lastAccessToken().get()

    /**
     * Last refresh token
     */
    fun getLastRefreshToken(): String = prefs.lastRefreshToken().get()

    /**
     * Last user
     */
    fun getLastUser(): User? {
        val userString = prefs.lastUser().get()

        return if (userString.isNullOrBlank()) {
            null
        } else {
            try {
                ObjectMapper().readValue(userString, User::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Last user accounts
     */
    fun getLastAccounts(): List<Account> {
        val accountsString = prefs.lastUserAccounts().get()

        return if (accountsString.isNullOrBlank()) {
            listOf()
        } else {
            try {
                val value: List<Account> = ObjectMapper().readValue(accountsString, object : TypeReference<List<Account>>() {})
                value.toList()
            } catch (e: Exception) {
                listOf<Account>()
            }
        }
    }

    fun clearUserInformation() {
        prefs.edit().accountType().put(-1)
                .lastRefreshToken().put("")
                .lastAccessToken().put("")
                .lastUser().put("")
                .lastUserAccounts().put("").apply()
    }

    fun storeUserInformation(loginResult: LoginResult) {
        prefs.edit().accountType().put(loginResult.socialAuthenticationType.value)
                .lastAccessToken().put(loginResult.accessToken)
                .lastRefreshToken().put(loginResult.refreshToken)
                .lastUser().put(ObjectMapper().writeValueAsString(loginResult.user))
                .lastUserAccounts().put(ObjectMapper().writeValueAsString(loginResult.accounts)).apply()
    }

    fun initUserInformation() {
        Config.refreshToken = getLastRefreshToken()
        Config.accessToken = getLastAccessToken()
        Config.user = getLastUser()
        Config.locale = getAppLocale()
        Config.accounts = getLastAccounts()
        Config.currentAccount = null
        Config.currentAccountNeedReload = true
    }
}