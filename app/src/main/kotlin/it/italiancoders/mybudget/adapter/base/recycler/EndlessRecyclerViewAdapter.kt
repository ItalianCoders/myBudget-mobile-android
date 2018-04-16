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