package it.italiancoders.mybudget.adapter.account

import android.content.Context
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.adapter.base.recycler.RecyclerViewAdapterBase
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.view.account.AccountView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 16/04/18
 */
class AccountAdapter(context: Context) : RecyclerViewAdapterBase<Account>(context) {

    interface AccountCallback {

        fun onAccountSelected(account: Account)
    }

    init {
        items = Config.accounts.orEmpty()
    }

    var accountCallback: AccountCallback?=null

    override fun onCreateItemView(context: Context): BindableView<Account> {
        val accountView = AccountView_.build(context)
        accountView.accountCallback = accountCallback
        return accountView
    }
}