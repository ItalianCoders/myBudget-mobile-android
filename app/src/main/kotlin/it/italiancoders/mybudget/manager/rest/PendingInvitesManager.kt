/*
 * Project: myBudget-mobile-android
 * File: PendingInvitesManager.kt
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
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.InviteStatus
import it.italiancoders.mybudget.rest.model.UserAccountInvite
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
open class PendingInvitesManager {

    @Bean
    lateinit var dialogManager: DialogManager

    fun load(context: Context, closure: Closure<List<UserAccountInvite>>) {
        dialogManager.openIndeterminateDialog(R.string.user_pending_invite_loading, context)
        RestClient.pendingInvitesService.load().enqueue(object : Callback<List<UserAccountInvite>> {
            override fun onResponse(call: Call<List<UserAccountInvite>>, response: Response<List<UserAccountInvite>>) {
                dialogManager.closeIndeterminateDialog()

                if (response.isSuccessful) {
                    closure.onSuccess(response.body().orEmpty())
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.user_pending_invite_error), Toast.LENGTH_SHORT).show()
                    closure.onFailure()
                }
            }

            override fun onFailure(call: Call<List<UserAccountInvite>>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.user_pending_invite_error), Toast.LENGTH_SHORT).show()
                closure.onError()
            }
        })
    }

    fun reply(context: Context, inviteId: String, inviteStatus: InviteStatus, closure: Closure<Void>) {
        dialogManager.openIndeterminateDialog(R.string.reply_sending, context)
        RestClient.pendingInvitesService.reply(inviteId, inviteStatus.value).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                dialogManager.closeIndeterminateDialog()
                if (response.isSuccessful) {
                    val text = if (inviteStatus == InviteStatus.Confirmed) context.resources.getString(R.string.confirmation_sent) else context.resources.getString(R.string.declined_sent)
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                    closure.onSuccess()
                } else {
                    Toast.makeText(context, context.resources.getString(R.string.reply_send_error), Toast.LENGTH_SHORT).show()
                    closure.onError()
                }
            }

            override fun onFailure(call: Call<Void>?, t: Throwable?) {
                dialogManager.closeIndeterminateDialog()
                Toast.makeText(context, context.resources.getString(R.string.reply_send_error), Toast.LENGTH_SHORT).show()
                closure.onFailure()
            }
        })
    }
}