/*
 * Project: myBudget-mobile-android
 * File: UserAccountInviteView.kt
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

package it.italiancoders.mybudget.view.useraccountinvite

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.PendingInvitesManager
import it.italiancoders.mybudget.rest.model.InviteStatus
import it.italiancoders.mybudget.rest.model.UserAccountInvite
import it.italiancoders.mybudget.utils.ImageUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EViewGroup(R.layout.item_user_account_invite)
open class UserAccountInviteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<UserAccountInvite> {

    @ViewById
    internal lateinit var aliasTV: TextView

    @ViewById
    internal lateinit var userImage: ImageView

    @Bean
    lateinit var pendingInvitesManager: PendingInvitesManager

    private lateinit var userAccountInvite: UserAccountInvite

    override fun bind(objectToBind: UserAccountInvite) {
        userAccountInvite = objectToBind

        aliasTV.text = objectToBind.invitedBy.alias.orEmpty()
        ImageUtils.loadImage(context, userImage, objectToBind.invitedBy.profileImageUrl, R.drawable.user)
    }

    @Click
    fun confirmButtonClicked() {
        sendReply(InviteStatus.Confirmed)
    }

    @Click
    fun rejectButtonClicked() {
        sendReply(InviteStatus.Declined)
    }

    private fun sendReply(inviteStatus: InviteStatus) {
        pendingInvitesManager.reply(context, userAccountInvite.id, inviteStatus, object : Closure<Void> {
            override fun onSuccess() {
                Config.currentAccountNeedReload = true
                (context as Activity).recreate()
            }
        })
    }
}