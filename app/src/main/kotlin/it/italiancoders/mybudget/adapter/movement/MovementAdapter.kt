package it.italiancoders.mybudget.adapter.movement

import android.content.Context
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.adapter.base.recycler.EndlessRecyclerViewAdapter
import it.italiancoders.mybudget.fragment.main.movements.MovementView_
import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.view.DayMovementView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/04/18
 */
class MovementAdapter(values: List<Movement>) : EndlessRecyclerViewAdapter<Movement>(values) {

    override fun buildItemView(context: Context, viewType: Int): BindableView<Movement> {
        return if (viewType == VIEW_TYPE_ITEM)
            MovementView_.build(context)
        else
            DayMovementView_.build(context)
    }

    override fun isGroupItem(item: Movement): Boolean {
        return item.id == null
    }
}