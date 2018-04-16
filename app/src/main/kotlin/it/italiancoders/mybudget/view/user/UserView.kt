package it.italiancoders.mybudget.view.user

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.User
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 10/04/18
 */
@EViewGroup(R.layout.item_user)
open class UserView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<User> {

    @ViewById
    internal lateinit var valueTV: TextView

    override fun bind(objectToBind: User) {

        valueTV.text = objectToBind.alias ?: objectToBind.username
    }
}