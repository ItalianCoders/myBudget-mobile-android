package it.italiancoders.mybudget.adapter.base


/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
interface BindableView<T> {

    fun bind(objectToBind: T)
}