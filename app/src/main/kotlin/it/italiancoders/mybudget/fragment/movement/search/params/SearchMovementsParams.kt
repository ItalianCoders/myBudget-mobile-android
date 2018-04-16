package it.italiancoders.mybudget.fragment.movement.search.params

import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.User
import java.io.Serializable

/**
 * @author fattazzo
 *         <p/>
 *         date: 06/04/18
 */
open class SearchMovementsParams(var anno: Int, var mese: Int, var giorno: Int? = null, var user: User? = null, var category: Category? = null, var page: Int = 0): Serializable