/*
 * Project: myBudget-mobile-android
 * File: EndlessRecyclerViewAdapter.kt
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

package it.italiancoders.mybudget.adapter.base.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView

abstract class EndlessRecyclerViewAdapter<in T>(private val values: List<T>) : RecyclerView.Adapter<EndlessRecyclerViewAdapter.ViewHolder>() {

    var serverListSize = -1

    open fun isGroupItem(item: T): Boolean {
        return true
    }

    override fun getItemViewType(position: Int): Int {
        return if (values.size in 1..position) VIEW_TYPE_LOADING else {
            if (isGroupItem(values[position])) {
                VIEW_TYPE_GROUP
            } else {
                VIEW_TYPE_ITEM
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return (if (getItemViewType(position) == VIEW_TYPE_ITEM) position else -1).toLong()
    }

    abstract fun buildItemView(context: Context, viewType: Int): BindableView<in T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView: View = if (viewType == VIEW_TYPE_LOADING) {
            inflater.inflate(R.layout.progress, parent, false)
        } else {
            val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val view = buildItemView(context, viewType)
            (view as View).layoutParams = lp
            view
        }

        return ViewHolder(contactView, viewType)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when (viewHolder.viewType) {
            VIEW_TYPE_LOADING -> {
                viewHolder.progressBar?.visibility = View.VISIBLE
                if (serverListSize in 1..position) {
                    // the ListView has reached the last row
                    viewHolder.progressBar?.visibility = View.GONE
                }
            }
            else -> {
                val contact = values[position]
                (viewHolder.itemView as BindableView<T>).bind(contact)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (values.isEmpty()) 0 else values.size + 1
    }

    class ViewHolder
    internal constructor(itemView: View, internal var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar? = itemView.findViewById(R.id.progressBar)

    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_GROUP = 2
    }
}