/*
 * Project: myBudget-mobile-android
 * File: AccountManager.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.manager.rest

import android.content.Context
import android.widget.Toast
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.DialogManager
import it.italiancoders.mybudget.rest.ErrorParser
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.*
import org.androidannotations.annotations.Background
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
                    Toast.makeText(context, context.resources.getString(R.string.account_saving_successful), Toast.LENGTH_SHORT).show()
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

    fun loadAccountDetails(context: Context, accountId: String, showLoadingDialog: Boolean = false, closure: Closure<AccountDetails?>) {
        if (showLoadingDialog) {
            dialogManager.openIndeterminateDialog(R.string.account_details_loading, context)
        }
        RestClient.accountService.getAccountDetails(accountId).enqueue(object : Callback<AccountDetails> {
            override fun onResponse(call: Call<AccountDetails>, response: Response<AccountDetails>) {
                if (showLoadingDialog) {
                    dialogManager.closeIndeterminateDialog()
                }
                if (response.isSuccessful) {
                    closure.onSuccess(response.body())
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.account_details_loading_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<AccountDetails>?, t: Throwable?) {
                if (showLoadingDialog) {
                    dialogManager.closeIndeterminateDialog()
                }
                Toast.makeText(context, context.resources.getString(R.string.account_details_loading_error), Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, context.resources.getString(R.string.account_saving_successful), Toast.LENGTH_SHORT).show()
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
    open fun loadAll(context: Context, closure: Closure<List<Account>>) {
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

    fun kickUser(context: Context, accountId: String, username: String, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.member_kicking, context)
        RestClient.accountService.kickUser(accountId, username).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    ErrorParser.parseAndShow(context, response.errorBody()?.string().orEmpty(), detailResId = R.string.member_kicking_error)
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.member_kicking_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun leave(context: Context, accountId: String, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.account_leaving, context)
        RestClient.accountService.left(accountId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.account_leaving_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.account_leaving_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun changeUserRole(context: Context, accountId: String, username: String, userRole: UserRole, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.role_changing, context)
        RestClient.accountService.changeUserRole(accountId, username, userRole.value).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    try {
                        ErrorParser.parseAndShow(context, response.errorBody()?.string().orEmpty(), detailResId = R.string.role_changing_error)
                        closure.onError()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.role_changing_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }
}