package it.italiancoders.mybudget.manager

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
interface Closure<T> {

    fun onSuccess(result: T) {

    }

    fun onSuccess() {

    }

    fun onFailure() {

    }

    fun onError() {

    }
}