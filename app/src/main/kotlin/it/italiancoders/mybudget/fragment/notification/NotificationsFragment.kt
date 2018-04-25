/*
 * Project: myBudget-mobile-android
 * File: NotificationsFragment.kt
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

package it.italiancoders.mybudget.fragment.notification

import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.fragment.notification.general.GeneralNotificationFragment_
import it.italiancoders.mybudget.fragment.notification.userinvite.UserInviteNotificationFragment_
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EFragment(R.layout.fragment_notifications)
open class NotificationsFragment : BaseFragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    enum class NotificationType { GENERAL, USER_INVITE }

    @JvmField
    @FragmentArg
    @InstanceState
    var notificationType = NotificationType.GENERAL

    @ViewById
    internal lateinit var bottomNavigationView: BottomNavigationView

    @AfterInject
    fun initInject() {
        println()
    }

    @AfterViews
    fun initView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        when (notificationType) {
            NotificationType.GENERAL -> bottomNavigationView.selectedItemId = R.id.menuBottomNotification
            NotificationType.USER_INVITE -> bottomNavigationView.selectedItemId = R.id.menuBottomNotificationUserInvite
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when (item.itemId) {
            R.id.menuBottomNotificationUserInvite -> {
                notificationType = NotificationType.USER_INVITE
                UserInviteNotificationFragment_.builder().build()
            }
            else -> {
                notificationType = NotificationType.GENERAL
                GeneralNotificationFragment_.builder().build()
            }
        }
        Handler().post({ FragmentUtils.replace(activity, fragment, containerResId = R.id.notification_container, animationType = FragmentUtils.AnimationType.SLIDE) })
        return true
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity, MainFragment_.builder().build(), animationType = FragmentUtils.AnimationType.FADE_IN)
        return true
    }

    override fun getActionBarTitleResId(): Int {
        return R.string.fragment_title_notification
    }
}