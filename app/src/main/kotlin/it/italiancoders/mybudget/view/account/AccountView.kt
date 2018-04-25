/*
 * Project: myBudget-mobile-android
 * File: AccountView.kt
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
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.account.AccountAdapter
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.Account
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/04/18
 */
@EViewGroup(R.layout.item_account)
open class AccountView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), BindableView<Account> {

    @ViewById
    internal lateinit var textTV: TextView

    @ViewById
    internal lateinit var membersTV: TextView

    @ViewById
    internal lateinit var descriptionTV: TextView

    var accountCallback: AccountAdapter.AccountCallback? = null

    lateinit var account: Account

    override fun bind(objectToBind: Account) {
        account = objectToBind

        textTV.text = objectToBind.name

        if (objectToBind.numberOfUsers ?: 1 > 1) {
            membersTV.visibility = View.VISIBLE
            membersTV.text = "${objectToBind.numberOfUsers}"
        } else {
            membersTV.visibility = View.GONE
        }

        descriptionTV.visibility = if (objectToBind.description.isNullOrBlank()) View.GONE else View.VISIBLE
        descriptionTV.text = objectToBind.description.orEmpty()
    }

    @Click
    fun rootLayoutClicked() {
        accountCallback?.onAccountSelected(account)
    }
}