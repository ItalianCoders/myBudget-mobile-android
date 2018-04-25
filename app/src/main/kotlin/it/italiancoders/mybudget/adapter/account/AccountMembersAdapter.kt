/*
 * Project: myBudget-mobile-android
 * File: AccountMembersAdapter.kt
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

package it.italiancoders.mybudget.adapter.account

import android.content.Context
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.adapter.base.recycler.RecyclerViewAdapterBase
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.view.account.AccountMemberView
import it.italiancoders.mybudget.view.account.AccountMemberView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
class AccountMembersAdapter(context: Context, var memberCallback: AccountMemberView.MemberCallback?) : RecyclerViewAdapterBase<User>(context) {

    init {
        items = Config.currentAccount?.members.orEmpty()
    }

    override fun onCreateItemView(context: Context): BindableView<User> {
        val view = AccountMemberView_.build(context)
        view.memberCallback = memberCallback
        return view
    }
}