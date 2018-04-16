package it.italiancoders.mybudget.manager.rest

import android.content.Context
import android.widget.Toast
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.DialogManager
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.AccountCreationRequest
import it.italiancoders.mybudget.rest.model.User
import org.androidannotations.annotations.Background
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class AccountManager {

    @Bean
    lateinit var dialogManager: DialogManager

    fun searchUser(context: Context, searchValue: String, closure: Closure<List<User>>): Call<List<User>> {
        val call = RestClient.accountService.searchUser(Config.currentAccount?.id.orEmpty(), searchValue)
        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (call.isCanceled) {
                    return
                }
                if (response.isSuccessful) {
                    closure.onSuccess(response.body().orEmpty())
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.searching_users_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<List<User>>?, t: Throwable?) {
                if (call != null && call.isCanceled) {
                    return
                }
                Toast.makeText(context, context.resources.getString(R.string.searching_users_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
        return call
    }

    fun inviteUser(context: Context, username: String) {
        dialogManager.openIndeterminateDialog(R.string.sending_user_invite, context)
        RestClient.accountService.inviteUser(Config.currentAccount?.id.orEmpty(), username).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    Toast.makeText(context, context.resources.getString(R.string.sending_user_invite_successfull), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.sending_user_invite_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.sending_user_invite_error), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun edit(context: Context, account: Account, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.account_saving, context)
        RestClient.accountService.edit(Config.currentAccount?.id.orEmpty(), account).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    Toast.makeText(context, context.resources.getString(R.string.account_saving_successfull), Toast.LENGTH_SHORT).show()
                    closure.onSuccess()
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.account_saving_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.account_saving_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun insert(context: Context, account: AccountCreationRequest, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.account_saving, context)
        RestClient.accountService.insert(account).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    Toast.makeText(context, context.resources.getString(R.string.account_saving_successfull), Toast.LENGTH_SHORT).show()
                    closure.onSuccess()
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.account_saving_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.account_saving_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    @Background
    open fun loadAll(context: Context,closure: Closure<List<Account>>) {
        dialogManager.openIndeterminateDialog(R.string.accounts_loading, context)
        try {
            val response = RestClient.accountService.getAll().execute()
            if (response.isSuccessful) {
                closure.onSuccess(response.body().orEmpty())
            }
        } catch (e: Exception) {
            closure.onError()
        } finally {
            dialogManager.closeIndeterminateDialog()
        }
    }
}