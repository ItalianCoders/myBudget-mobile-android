/*
 * Project: myBudget-mobile-android
 * File: AccountMemberView.kt
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

package it.italiancoders.mybudget.view.account

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.*
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.AccountManager
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.rest.model.UserRole
import it.italiancoders.mybudget.utils.ImageUtils
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
@EViewGroup(R.layout.item_account_member)
open class AccountMemberView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<User> {

    interface MemberCallback {

        fun onMemberKicked()

        fun onCurrentUserLeave()
    }

    @Bean
    lateinit var accountManager: AccountManager

    @ViewById
    internal lateinit var aliasTV: TextView

    @ViewById
    internal lateinit var adminSwitch: Switch

    @ViewById
    internal lateinit var userImage: ImageView

    @ViewById
    internal lateinit var leaveKickTV: TextView

    private var bindChange = false

    lateinit var user: User

    var memberCallback: MemberCallback? = null

    override fun bind(objectToBind: User) {
        user = objectToBind
        try {
            bindChange = true

            aliasTV.text = objectToBind.alias.orEmpty()
            adminSwitch.isChecked = isAdmin()

            if (user.username == Config.user?.username ?: "_") {
                leaveKickTV.text = resources.getString(R.string.leave)
                leaveKickTV.setTextColor(ContextCompat.getColor(context,R.color.light_red))
            } else {
                leaveKickTV.text = resources.getString(R.string.kick)
                leaveKickTV.setTextColor(ContextCompat.getColor(context,android.R.color.holo_blue_dark))
            }
        } finally {
            bindChange = false
        }

        ImageUtils.loadImage(context, userImage, objectToBind.profileImageUrl, R.drawable.user)
    }

    private fun isAdmin(): Boolean {
        return Config.currentAccount?.administrators.orEmpty().contains(user.username)
    }

    @CheckedChange
    fun adminSwitchCheckedChanged(compoundButton: CompoundButton, isChecked: Boolean) {
        if (!bindChange) {
            val userRole = if (isChecked) UserRole.Admin else UserRole.Standard
            accountManager.changeUserRole(context, Config.currentAccount?.id.orEmpty(), user.username, userRole, object : Closure<Void> {
                override fun onError() {
                    bindChange = true
                    adminSwitch.isChecked = !adminSwitch.isChecked
                    bindChange = false
                }

                override fun onFailure() {
                    bindChange = true
                    adminSwitch.isChecked = !adminSwitch.isChecked
                    bindChange = false
                }
            })
        }
    }

    @Click
    fun leaveKickTVClicked() {
        if (user.username == Config.user?.username ?: "_") {
            leave()
        } else {
            kickUser()
        }
    }

    private fun kickUser() {
        MaterialDialog.Builder(context)
                .iconRes(R.drawable.exit)
                .title(R.string.member_kicking_dialog_title)
                .content(R.string.member_kicking_dialog_message, user.alias, Config.currentAccount?.name.orEmpty())
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ ->
                    accountManager.kickUser(context, Config.currentAccount?.id.orEmpty(), user.username, object : Closure<Void> {
                        override fun onSuccess() {
                            memberCallback?.onMemberKicked()
                        }
                    })
                }
                .show()
    }

    private fun leave() {
        MaterialDialog.Builder(context)
                .iconRes(R.drawable.exit)
                .title(R.string.account_leaving_dialog_title)
                .content(R.string.account_leaving_dialog_message, Config.currentAccount?.name.orEmpty())
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ ->
                    accountManager.leave(context, Config.currentAccount?.id.orEmpty(), object : Closure<Void> {
                        override fun onSuccess() {
                            memberCallback?.onCurrentUserLeave()
                        }
                    })
                }
                .show()
    }
}