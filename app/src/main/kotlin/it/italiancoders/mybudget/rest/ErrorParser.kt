/*
 * Project: myBudget-mobile-android
 * File: ErrorParser.kt
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

package it.italiancoders.mybudget.rest

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.rest.model.Error

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
object ErrorParser {

    private fun parse(context: Context, jsonError: String?, titleResId: Int = R.string.error, detailResId: Int = R.string.error_try_later): Error {
        val defaultError = Error()
        defaultError.title = context.resources.getString(titleResId)
        defaultError.detail = context.resources.getString(detailResId)

        jsonError?.let {
            return try {
                val error = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(jsonError, Error::class.java)
                error.title = error.title ?: defaultError.title
                error.detail = error.detail ?: defaultError.detail
                error
            } catch (e: Exception) {
                defaultError
            }
        }
        return defaultError
    }

    fun parseAndShow(context: Context, jsonError: String?, titleResId: Int = R.string.error, detailResId: Int = R.string.error_try_later) {
        val error = parse(context, jsonError, titleResId, detailResId)
        MaterialDialog.Builder(context)
                .iconRes(R.drawable.cancel)
                .title(error.title.orEmpty())
                .content(error.detail.orEmpty())
                .positiveText(android.R.string.ok)
                .show()
    }
}