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
    }

    @Click
    fun rootLayoutClicked() {
        accountCallback?.onAccountSelected(account)
    }
}