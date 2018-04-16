package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import java.io.Serializable


/**
 * @author fattazzo
 *         <p/>
 *         date: 28/03/18
 */
class RegistrationUser : Serializable {

    var username: String? = null

    var password: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var email: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var firstname: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var lastname: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var gender: GenderEnum? = null

    fun isValid(): Boolean {
        return username.orEmpty().isNotEmpty() && password.orEmpty().isNotEmpty() && email.orEmpty().isNotEmpty()
                && firstname.orEmpty().isNotEmpty() && lastname.orEmpty().isNotEmpty()
    }
}