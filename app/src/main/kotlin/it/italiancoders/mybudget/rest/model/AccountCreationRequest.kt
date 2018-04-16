package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable

/**
 * @author fattazzo
 *         <p/>
 *         date: 15/04/18
 */
class AccountCreationRequest : Serializable {

    lateinit var name: String

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var description: String? = null

    var initialBalance: Double = 10.0
}