package it.italiancoders.mybudget.adapter

import android.content.Context
import it.italiancoders.mybudget.adapter.base.BaseFilterAdapter
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.view.CategoryView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
class CategoryAdapter(context: Context, mValues: List<Category>, extraItem: Category? = null) :
        BaseFilterAdapter<Category>(context, mValues, extraItem) {

    override fun matchFilter(item: Category, constraint: CharSequence): Boolean {
        return item.value!!.toLowerCase().contains(constraint)
    }

    override fun buidView(): BindableView<Category> = CategoryView_.build(context)
}