package it.italiancoders.mybudget.rest.model

import java.io.Serializable

/**
 * @author fattazzo
 *         <p/>
 *         date: 18/04/18
 */
class Error : Serializable {

    var title: String? = null

    var detail: String? = null

    val timeStamp: Long? = null

    val developerMessage: String? = null

    val exception: String? = null

    val code: Int? = null

    val subcode: Int? = null
}