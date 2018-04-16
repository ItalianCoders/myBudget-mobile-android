package it.italiancoders.mybudget.adapter.user

import android.content.Context
import it.italiancoders.mybudget.adapter.base.BaseFilterAdapter
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.view.user.UserInviteView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
class UserInviteAdapter(context: Context, mValues: List<User>) :
        BaseFilterAdapter<User>(context, mValues, null) {

    override fun buidView(): BindableView<User> = UserInviteView_.build(context)
}