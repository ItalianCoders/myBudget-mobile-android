package it.italiancoders.mybudget.adapter.movement

import it.italiancoders.mybudget.rest.model.Movement

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
class DayMovement(day: Long) : Movement() {

    init {
        executedAt = day
    }
}