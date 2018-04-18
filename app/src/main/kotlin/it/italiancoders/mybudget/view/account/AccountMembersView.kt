package it.italiancoders.mybudget.view.account

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.account.AccountMembersAdapter
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.AccountManager
import it.italiancoders.mybudget.rest.model.AccountDetails
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
@EViewGroup(R.layout.view_account_members)
open class AccountMembersView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Bean
    lateinit var accountManager: AccountManager

    @ViewById
    internal lateinit var accountMembersRecyclerView: RecyclerView

    var memberCallback: AccountMemberView.MemberCallback? = null

    @AfterViews
    fun initViews() {
        accountMembersRecyclerView.layoutManager = LinearLayoutManager(context)
        accountMembersRecyclerView.adapter = AccountMembersAdapter(context,memberCallback)
    }

    open fun updateView(accountId: String) {
        accountManager.loadAccountDetails(context, accountId, true, object : Closure<AccountDetails?> {
            override fun onSuccess(result: AccountDetails?) {
                Config.currentAccount = result

                accountMembersRecyclerView.adapter = AccountMembersAdapter(context,memberCallback)
            }
        })
    }
}