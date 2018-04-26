/*
 * Project: myBudget-mobile-android
 * File: ScheduledMovementsManager.kt
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
import it.italiancoders.mybudget.rest.model.ScheduledMovementSettings
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/04/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class ScheduledMovementsManager {

    @Bean
    lateinit var dialogManager: DialogManager

    fun loadAll(context: Context, closure: Closure<List<ScheduledMovementSettings>>) {
        dialogManager.openIndeterminateDialog(R.string.scheduled_movements_loading, context)
        RestClient.scheduledMovementService.loadAll(Config.currentAccount?.id.orEmpty()).enqueue(object : Callback<List<ScheduledMovementSettings>> {
            override fun onResponse(call: Call<List<ScheduledMovementSettings>>, response: Response<List<ScheduledMovementSettings>>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess(response.body().orEmpty())
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.scheduled_movements_loading_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<List<ScheduledMovementSettings>>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.scheduled_movements_loading_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }

    fun save(context: Context, scheduledMovementSettings: ScheduledMovementSettings, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.scheduled_movement_saving, context)
        RestClient.scheduledMovementService.save(Config.currentAccount?.id.orEmpty(), scheduledMovementSettings).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    ErrorParser.parseAndShow(context,response.errorBody()?.string().orEmpty())
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

    fun update(context: Context, scheduledMovementSettings: ScheduledMovementSettings, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.scheduled_movement_saving, context)
        RestClient.scheduledMovementService.update(Config.currentAccount?.id.orEmpty(), scheduledMovementSettings.id, scheduledMovementSettings).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    ErrorParser.parseAndShow(context,response.errorBody()?.string().orEmpty())
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

    fun delete(context: Context, settingsId: String, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.scheduled_movement_saving, context)
        RestClient.scheduledMovementService.delete(Config.currentAccount?.id.orEmpty(), settingsId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    closure.onSuccess()
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
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