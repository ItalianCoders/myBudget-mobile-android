package it.italiancoders.mybudget.adapter.base.recycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import it.italiancoders.mybudget.adapter.base.BindableView
import java.util.*

abstract class RecyclerViewAdapterBase<T>(private val context: Context) : RecyclerView.Adapter<ViewWrapper<T>>() {

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
    }

    companion object {
        const val VIEW_TYPE_ITEM = 1
        const val VIEW_TYPE_GROUP = 2
    }
}