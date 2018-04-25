/*
 * Project: myBudget-mobile-android
 * File: UserInviteNotificationFragment.kt
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

package it.italiancoders.mybudget.fragment.notification.userinvite

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.useraccountinvitegroup.UserAccountInviteAdapter
import it.italiancoders.mybudget.adapter.useraccountinvitegroup.UserAccountInviteGroup
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.PendingInvitesManager
import it.italiancoders.mybudget.rest.model.UserAccountInvite
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EFragment(R.layout.fragment_notification_userinvite)
open class UserInviteNotificationFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    @Bean
    lateinit var pendingInvitesManager: PendingInvitesManager

    @JvmField
    @ViewById
    internal var inviteSwipeRefreshLayout: SwipeRefreshLayout? = null

    @ViewById
    internal lateinit var inviteRecyclerView: RecyclerView

    private var userAccountInvites = arrayListOf<UserAccountInvite>()

    private val userAccountInviteAdapter: UserAccountInviteAdapter by lazy { UserAccountInviteAdapter(context!!) }

    @AfterViews
    fun initViews() {
        inviteRecyclerView.adapter = userAccountInviteAdapter
        inviteRecyclerView.layoutManager = LinearLayoutManager(context)

        onRefresh()

        inviteSwipeRefreshLayout?.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        pendingInvitesManager.load(context!!, object : Closure<List<UserAccountInvite>>{
            override fun onSuccess(result: List<UserAccountInvite>) {
                userAccountInvites.clear()
                userAccountInvites.addAll(result)
                updateUI()
            }
            override fun onFailure() {
                userAccountInvites.clear()
                updateUI()
            }
            override fun onError() {
                updateUI()
            }
        })
    }

    private fun updateUI() {
        if (inviteSwipeRefreshLayout == null) return

        inviteSwipeRefreshLayout?.isRefreshing = false
        userAccountInviteAdapter.items = buildInviteGroups(userAccountInvites)
    }

    private fun buildInviteGroups(originalInvites: List<UserAccountInvite>): List<UserAccountInvite> {
        var currentDay = ""

        val inviteGroups = mutableListOf<UserAccountInvite>()
        originalInvites.forEach {
            val movDay = DataUtils.formatLocalDate(it.createdAt ?: 0L)
            if (currentDay != movDay) {
                currentDay = movDay
                val dayGroup = UserAccountInviteGroup(it.createdAt ?: 0L)
                inviteGroups.add(dayGroup)
            }
            inviteGroups.add(it)
        }
        return inviteGroups.toList()
    }
}