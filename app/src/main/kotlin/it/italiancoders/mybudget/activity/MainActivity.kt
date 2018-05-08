/*
 * Project: myBudget-mobile-android
 * File: MainActivity.kt
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

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.appus.splash.Splash
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.budget.BudgetsFragment_
import it.italiancoders.mybudget.fragment.login.WelcomeFragment_
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.fragment.movement.auto.ScheduledMovementsFragment_
import it.italiancoders.mybudget.fragment.movement.search.SearchMovementsFragment_
import it.italiancoders.mybudget.manager.rest.AuthManager
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_ID
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_PWD
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_TOKEN
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.EXTRA_LOGIN_TYPE
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.LOGIN_ACTIVITY_REQUEST_CODE
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_LOGIN
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_LOGOUT
import it.italiancoders.mybudget.manager.rest.AuthManager.Companion.RESULT_ACC_REMOVE
import it.italiancoders.mybudget.preferences.ApplicationPreferenceManager
import it.italiancoders.mybudget.rest.model.SocialTypeEnum
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.utils.FragmentUtils
import it.italiancoders.mybudget.utils.ImageUtils
import it.italiancoders.mybudget.utils.LocaleHelper
import it.italiancoders.mybudget.utils.PermissionsUtil
import org.androidannotations.annotations.*


@EActivity
open class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, Config.UserListener {

    @ViewById
    internal lateinit var navView: NavigationView

    @ViewById
    internal lateinit var drawerLayout: DrawerLayout

    @ViewById
    internal lateinit var toolbar: Toolbar

    internal lateinit var currentUserTV: TextView
    internal lateinit var currentUserImage: ImageView
    internal lateinit var editCurrentUserImage: ImageView

    @Bean
    lateinit var preferenceManager: ApplicationPreferenceManager

    @Bean
    lateinit var authManager: AuthManager

    @AfterViews
    protected fun init() {
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.itemIconTintList = null
        val headerView = navView.getHeaderView(0)

        currentUserTV = headerView.findViewById(R.id.currentUserTV)
        currentUserImage = headerView.findViewById(R.id.currentUserImage)
        editCurrentUserImage = headerView.findViewById(R.id.editCurrentUserImage)

        Config.userListener = this
        onChange(null, Config.user)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LocaleHelper.setLocale(this, preferenceManager.getAppLocale())

        if (savedInstanceState == null) {
            val imageSplash = ImageUtils.getBitmap(ContextCompat.getDrawable(this, R.drawable.splash_image) as VectorDrawable)
            val splash = Splash.Builder(this, null)
                    .setAnimationType(Splash.AnimationType.TYPE_2)
                    .setSplashImage(BitmapDrawable(resources, imageSplash))//resources.getDrawable(R.drawable.pig))
                    .setOneShotStart(false)
                    .setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor))
            splash.perform()

            PermissionsUtil.askPermissions(this)

            preferenceManager.initUserInformation()
            when {
                preferenceManager.getAccountType() == -1 -> FragmentUtils.replace(this, WelcomeFragment_.builder().build())
                Config.accessToken == null -> authManager.login(SocialTypeEnum.fromValue(preferenceManager.getAccountType()), this)
                else -> FragmentUtils.replace(this, MainFragment_.builder().build())
            }
        }
    }

    @OnActivityResult(LOGIN_ACTIVITY_REQUEST_CODE)
    fun onLoginResult(resultCode: Int, data: Intent?) {
        when (resultCode) {
            RESULT_ACC_LOGIN -> {
                val token = data!!.getStringExtra(EXTRA_LOGIN_TOKEN).orEmpty()
                val id = data.getStringExtra(EXTRA_LOGIN_ID).orEmpty()
                val type = SocialTypeEnum.fromValue(data.getIntExtra(EXTRA_LOGIN_TYPE, -1))
                        ?: SocialTypeEnum.None
                val pwd = data.getStringExtra(EXTRA_LOGIN_PWD).orEmpty()

                authManager.login(id, pwd, type, token, this)
            }
            RESULT_ACC_LOGOUT -> authManager.logout(this)
            RESULT_ACC_REMOVE -> authManager.remove(this)
        }
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_settings -> PreferenceActivity_.intent(this).start()
            R.id.nav_logout -> authManager.logout(this)
            R.id.nav_search_movements -> FragmentUtils.replace(this, SearchMovementsFragment_.builder().build())
            R.id.nav_auto_movements -> FragmentUtils.replace(this, ScheduledMovementsFragment_.builder().build())
            R.id.nav_budgets -> FragmentUtils.replace(this, BudgetsFragment_.builder().build())
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onChange(oldUser: User?, newUser: User?) {
        currentUserTV.text = ""
        currentUserTV.visibility = View.GONE
        currentUserImage.visibility = View.GONE
        editCurrentUserImage.visibility = View.GONE

        newUser?.let {
            currentUserTV.text = newUser.alias ?: newUser.username ?: newUser.email
            currentUserTV.visibility = View.VISIBLE
            currentUserImage.visibility = View.VISIBLE
            editCurrentUserImage.visibility = View.VISIBLE
            ImageUtils.loadImage(this, currentUserImage, it.profileImageUrl, R.drawable.user)
        }
    }

    fun updateAccountTitle() {
        val accountTV = findViewById<TextView>(R.id.accountTV)
        accountTV.text = Config.currentAccount?.name.orEmpty()

        val accountMembersTV = findViewById<TextView>(R.id.accountMembersTV)
        accountMembersTV.text = "${Config.currentAccount?.numberOfUsers ?: 1}"

        val accountMembersLayout = findViewById<ConstraintLayout>(R.id.accountMembersLayout)
        accountMembersLayout.visibility = if (Config.currentAccount?.numberOfUsers ?: 1 > 1) View.VISIBLE else View.GONE
    }

    fun toggleAccountTitle(visible: Boolean) {
        val accountLayout = findViewById<View>(R.id.accountLayout)
        if (accountLayout != null) {
            accountLayout.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }

        val fragmentManager = supportFragmentManager

        val fragments = fragmentManager.fragments

        val li = fragments.listIterator(fragments.size)
        while (li.hasPrevious()) {
            val fragment = li.previous() as Fragment
            if (fragment is BaseFragment) {
                val done = fragment.backPressed()
                if (done) {
                    break
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionsUtil.PERMISSION_ALL -> {
                PermissionsUtil.processPermission(this, permissions, grantResults)
            }
        }
    }
}
