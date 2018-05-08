/*
 * Project: myBudget-mobile-android
 * File: BudgetManager.kt
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
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.DialogManager
import it.italiancoders.mybudget.rest.ErrorParser
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.Budget
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author fattazzo
 *         <p/>
 *         date: 07/05/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class BudgetManager {

    @Bean
    lateinit var dialogManager: DialogManager

    fun loadAll(context: Context, accountId: String, closure: Closure<List<Budget>>) {
        dialogManager.openIndeterminateDialog(R.string.budgets_account_loading, context)
        RestClient.budgetService.loadAll(accountId).enqueue(object : Callback<List<Budget>> {
            override fun onResponse(call: Call<List<Budget>>, response: Response<List<Budget>>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess(response.body().orEmpty())
                } else {
                    ErrorParser.parseAndShow(context, response.errorBody()?.string())
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<List<Budget>>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun delete(context: Context, accountId: String, budgetId: String, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.budget_deleting, context)
        RestClient.budgetService.delete(accountId, budgetId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    ErrorParser.parseAndShow(context, response.errorBody()?.string())
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun edit(context: Context, accountId: String, budget: Budget, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.budget_saving, context)
        RestClient.budgetService.edit(accountId, budget.id!!, budget).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    ErrorParser.parseAndShow(context, response.errorBody()?.string())
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun insert(context: Context, accountId: String, budget: Budget, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.budget_saving, context)
        RestClient.budgetService.insert(accountId, budget).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    ErrorParser.parseAndShow(context, response.errorBody()?.string())
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }
}