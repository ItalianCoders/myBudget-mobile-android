package it.italiancoders.mybudget.adapter.base.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import it.italiancoders.mybudget.adapter.base.BindableView

class ViewWrapper<T>(val view: BindableView<T>) : RecyclerView.ViewHolder(view as View)