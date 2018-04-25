/*
 * Project: myBudget-mobile-android
 * File: BaseFragment.kt
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

package it.italiancoders.mybudget.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.MainActivity
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.UiThread


/**
 * @author fattazzo
 *         <p/>
 *         date: 25/01/18
 */
@EFragment
open class BaseFragment : Fragment() {

    private var dialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun backPressed(): Boolean {
        return false
    }

    open fun openIndeterminateDialog(title: String) {
        openIndeterminateDialog(title, activity!!)
    }

    open fun openIndeterminateDialog(titleResId: Int) {
        openIndeterminateDialog(resources.getString(titleResId), activity!!)
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

    private fun setActionBarTitle() {
        if (getActionBarTitle() == null) {
            (activity as AppCompatActivity).supportActionBar?.setTitle(getActionBarTitleResId())
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = getActionBarTitle()
        }
    }

    open fun getActionBarTitleResId(): Int {
        return R.string.app_name
    }

    open fun getActionBarTitle(): String? {
        return null
    }

    open fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {}

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        createFragmentOptionMenu(menu, inflater)

        for (i in 0 until (menu?.size() ?: 0)) {
            val drawable = menu?.getItem(i)?.icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(ContextCompat.getColor(activity!!, R.color.primaryTextColorInverted), PorterDuff.Mode.SRC_ATOP)
            }
        }
        updateAccountsSelectionView()
        setActionBarTitle()
    }

    private fun updateAccountsSelectionView() {
        (activity!! as MainActivity).toggleAccountTitle(isAccountsSelectionVisible())
    }

    open fun isAccountsSelectionVisible(): Boolean {
        return false
    }
}