/*
 * Project: myBudget-mobile-android
 * File: DialogManager.kt
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

package it.italiancoders.mybudget.manager

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.UiThread

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EBean
open class DialogManager {

    private var dialog: MaterialDialog? = null

    open fun openIndeterminateDialog(titleResId: Int, context: Context) {
        openIndeterminateDialog(context.resources.getString(titleResId), context)
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun openIndeterminateDialog(title: String, context: Context) {
        if (dialog == null || !dialog!!.isShowing) {
            dialog = MaterialDialog.Builder(context)
                    .title(title)
                    .content(R.string.dialog_wait_content)
                    .progress(true, 0)
                    .cancelable(false)
                    .build()
            dialog!!.show()
        }
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun closeIndeterminateDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
}