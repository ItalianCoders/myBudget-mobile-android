package it.italiancoders.mybudget.view.useraccountinvite

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.UserAccountInvite
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EViewGroup(R.layout.item_day_view)
open class UserAccountInviteGroupView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<UserAccountInvite> {

    @ViewById
    internal lateinit var dayTV: TextView

    @ViewById
    internal lateinit var monthTV: TextView

    @ViewById
    internal lateinit var yearTV: TextView

    override fun bind(objectToBind: UserAccountInvite) {

        dayTV.text = DataUtils.formatLocalDate(objectToBind.createdAt ?: 0, pattern = "dd")
        monthTV.text = DataUtils.formatLocalDate(objectToBind.createdAt ?: 0, pattern = "MMMM")
        yearTV.text = DataUtils.formatLocalDate(objectToBind.createdAt ?: 0, pattern = "yyyy")
    }
}