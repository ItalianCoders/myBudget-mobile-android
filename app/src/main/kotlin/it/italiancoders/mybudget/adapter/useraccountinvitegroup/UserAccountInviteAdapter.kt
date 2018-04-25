/*
 * Project: myBudget-mobile-android
 * File: UserAccountInviteAdapter.kt
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

package it.italiancoders.mybudget.adapter.useraccountinvitegroup

import android.content.Context
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.adapter.base.recycler.RecyclerViewAdapterBase
import it.italiancoders.mybudget.rest.model.UserAccountInvite
import it.italiancoders.mybudget.view.useraccountinvite.UserAccountInviteGroupView_
import it.italiancoders.mybudget.view.useraccountinvite.UserAccountInviteView
import it.italiancoders.mybudget.view.useraccountinvite.UserAccountInviteView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
class UserAccountInviteAdapter(context: Context) : RecyclerViewAdapterBase<UserAccountInvite>(context) {

    override fun isGroupItem(item: UserAccountInvite): Boolean {
        return item is UserAccountInviteGroup
    }

    override fun onCreateItemView(context: Context): UserAccountInviteView = UserAccountInviteView_.build(context)

    override fun onCreateGroupView(context: Context): BindableView<UserAccountInvite> {
        return UserAccountInviteGroupView_.build(context)
    }
}