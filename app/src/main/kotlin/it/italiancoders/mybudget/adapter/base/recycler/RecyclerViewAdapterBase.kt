/*
 * Project: myBudget-mobile-android
 * File: RecyclerViewAdapterBase.kt
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
import android.view.View
import android.view.ViewGroup
import it.italiancoders.mybudget.adapter.base.BindableView
import java.util.*

abstract class RecyclerViewAdapterBase<T>(private val context: Context, var onItemClickListener: OnItemClickListener<T>? = null) : RecyclerView.Adapter<ViewWrapper<T>>() {

    interface OnItemClickListener<T> {
        fun onItemClick(item: T?)
    }

    var items: List<T> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return items.size
    }

    open fun isGroupItem(item: T): Boolean {
        return false
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGroupItem(items[position])) VIEW_TYPE_GROUP else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewWrapper<T> {
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val wrapper = if (viewType == VIEW_TYPE_GROUP) {
            ViewWrapper(onCreateGroupView(context))
        } else {
            ViewWrapper(onCreateItemView(context))
        }
        (wrapper.view as View).layoutParams = lp
        return wrapper
    }

    protected abstract fun onCreateItemView(context: Context): BindableView<T>

    open fun onCreateGroupView(context: Context): BindableView<T> = onCreateItemView(context)

    override fun onBindViewHolder(holder: ViewWrapper<T>, position: Int) {
        holder.view.bind(items[position])
        holder.item = items[position]
        onItemClickListener?.let {
            (holder.view as View).setOnClickListener { onItemClickListener?.onItemClick(holder.item) }
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_GROUP = 2
    }
}