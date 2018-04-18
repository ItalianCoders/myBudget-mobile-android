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