package it.italiancoders.mybudget.adapter.user

import android.content.Context
import it.italiancoders.mybudget.adapter.base.BaseFilterAdapter
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.view.user.UserView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
class UserAdapter(context: Context, mValues: List<User>, extraItem: User? = null) :
        BaseFilterAdapter<User>(context, mValues, extraItem) {

    override fun matchFilter(item: User, constraint: CharSequence): Boolean {
        return item.alias.orEmpty().toLowerCase().contains(constraint) || item.username.toLowerCase().contains(constraint)
    }

    override fun buidView(): BindableView<User> = UserView_.build(context)
}