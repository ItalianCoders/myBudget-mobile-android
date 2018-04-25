/*
 * Project: myBudget-mobile-android
 * File: BaseFilterAdapter.kt
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

package it.italiancoders.mybudget.adapter.base

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
abstract class BaseFilterAdapter<T>(protected var context: Context, private var mValues: List<T>, private val extraItem: T? = null) : BaseAdapter(), Filterable {

    private var mBackupValues: List<T> = mValues
    private val mFilter = StringFilter()

    private fun getExtraItemCount(): Int = if (extraItem == null) 0 else 1

    override fun getCount(): Int {
        return mValues.size + getExtraItemCount()
    }

    override fun getItem(position: Int): T? {
        return if (position == 0) {
            extraItem ?: mValues[position - getExtraItemCount()]
        } else {
            mValues[position - getExtraItemCount()]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    abstract fun buidView(): BindableView<T>

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: BindableView<T> = if (convertView == null) {
            buidView()
        } else {
            convertView as BindableView<T>
        }

        val itemToBind: T = if (position == 0) {
            extraItem ?: getItem(position)!!
        } else {
            getItem(position)!!
        }

        view.bind(itemToBind)
        return view as View
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    open fun matchFilter(item: T, constraint: CharSequence): Boolean {
        return true
    }

    inner class StringFilter : Filter() {

        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {
            val filterResults = Filter.FilterResults()
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupValues.size
                filterResults.values = mBackupValues
                return filterResults
            }
            val filterItems = ArrayList<T>()
            for (item in mBackupValues) {
                if (matchFilter(item, constraint)) {
                    filterItems.add(item)
                }
            }
            filterResults.count = filterItems.size
            filterResults.values = filterItems
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
            mValues = results.values as ArrayList<T>
            notifyDataSetChanged()
        }
    }

    open fun setValues(items: List<T>) {
        mValues = items
        mBackupValues = items
        notifyDataSetChanged()
        notifyDataSetInvalidated()
    }
}