/*
 * Project: myBudget-mobile-android
 * File: AccountFragment.kt
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

package it.italiancoders.mybudget.fragment.account

import android.support.design.widget.TextInputEditText
import android.view.View
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.AccountManager
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.AccountCreationRequest
import it.italiancoders.mybudget.utils.FragmentUtils
import it.italiancoders.mybudget.view.account.AccountMemberView
import it.italiancoders.mybudget.view.account.AccountMembersView
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 14/04/18
 */
@EFragment(R.layout.fragment_account)
open class AccountFragment : BaseFragment(), AccountMemberView.MemberCallback {

    @JvmField
    @FragmentArg
    @InstanceState
    var account: Account? = null

    @ViewById
    internal lateinit var nameTIET: TextInputEditText

    @ViewById
    internal lateinit var descriptionTIET: TextInputEditText

    @ViewById
    internal lateinit var accountMembersView: AccountMembersView

    @Bean
    lateinit var accountManager: AccountManager

    @AfterViews
    fun iniViews() {
        nameTIET.text.clear()
        descriptionTIET.text.clear()
        accountMembersView.visibility = if (account == null) View.GONE else View.VISIBLE
        accountMembersView.memberCallback = this

        if (account != null) {
            nameTIET.text.append(account!!.name)
            descriptionTIET.text.append(account!!.defaultUsername.orEmpty())
            accountMembersView.updateView(account!!.id)
        } else {
            nameTIET.text.append(getString(R.string.new_m_action))
        }
    }

    @Click
    fun saveButtonClicked() {
        nameTIET.error = null
        if (nameTIET.text.toString().isEmpty()) {
            nameTIET.error = getString(R.string.required)
            return
        }
        if (account != null) {
            account!!.name = nameTIET.text.toString()
            account!!.description = descriptionTIET.text.toString()
            accountManager.edit(context!!, account!!, object : Closure<Void> {
                override fun onSuccess() {
                    Config.currentAccountNeedReload = true
                    backPressed()
                }
            })
        } else {
            val accountCreationRequest = AccountCreationRequest()
            accountCreationRequest.name = nameTIET.text.toString()
            accountCreationRequest.description = descriptionTIET.text.toString()
            accountManager.insert(context!!, accountCreationRequest, object : Closure<Void> {
                override fun onSuccess() {
                    backPressed()
                }
            })
        }
    }

    override fun onMemberKicked() {
        accountMembersView.updateView(account!!.id)
    }

    override fun onCurrentUserLeave() {
        Config.currentAccount = null
        Config.currentAccountNeedReload = true
        accountManager.loadAll(context!!, object : Closure<List<Account>> {
            override fun onSuccess(result: List<Account>) {
                Config.accounts = result
                backPressed()
            }
        })
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity, MainFragment_.builder().build(), animationType = FragmentUtils.AnimationType.SLIDE)
        return true
    }

    override fun getActionBarTitle(): String? {
        val action = getString(if (account == null) R.string.new_m_action else R.string.edit_action)
        return "$action ${getString(R.string.account)}"
    }
}