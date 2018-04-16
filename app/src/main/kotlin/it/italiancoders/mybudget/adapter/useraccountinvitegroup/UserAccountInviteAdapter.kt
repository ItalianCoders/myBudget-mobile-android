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