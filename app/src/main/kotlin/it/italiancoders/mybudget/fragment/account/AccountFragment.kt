package it.italiancoders.mybudget.fragment.account

import android.support.design.widget.TextInputEditText
import android.view.Menu
import android.view.MenuInflater
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.AccountManager
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.AccountCreationRequest
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 14/04/18
 */
@EFragment(R.layout.fragment_account)
open class AccountFragment : BaseFragment() {

    @JvmField
    @FragmentArg
    @InstanceState
    var account: Account? = null

    @ViewById
    internal lateinit var nameTIET: TextInputEditText

    @ViewById
    internal lateinit var descriptionTIET: TextInputEditText

    @Bean
    lateinit var accountManager: AccountManager

    @AfterViews
    fun iniViews() {
        nameTIET.text.clear()
        descriptionTIET.text.clear()

        if (account != null) {
            nameTIET.text.append(account!!.name)
            descriptionTIET.text.append(account!!.defaultUsername.orEmpty())
        } else {
            nameTIET.text.append(getString(R.string.new_m_action))
        }
    }

    @OptionsItem
    fun menuAccountSaveSelected() {
        if (account != null) {
            account!!.name = nameTIET.text.toString()
            account!!.description = descriptionTIET.text.toString()
            accountManager.edit(context!!, account!!, object : Closure<Void> {
                override fun onSuccess() {
                    Config.currentAccountNeedReload = true
                    backPressed()
                }
            })
        } else {
            val accountCreationRequest = AccountCreationRequest()
            accountCreationRequest.name = nameTIET.text.toString()
            accountCreationRequest.description = descriptionTIET.text.toString()
            accountManager.insert(context!!, accountCreationRequest, object : Closure<Void> {
                override fun onSuccess() {
                    backPressed()
                }
            })
        }
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.account, menu)
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity, MainFragment_.builder().build(), animationType = FragmentUtils.AnimationType.SLIDE)
        return true
    }

    override fun getActionBarTitle(): String? {
        val action = getString(if (account == null) R.string.new_m_action else R.string.edit_action)
        return "$action ${getString(R.string.account)}"
    }
}