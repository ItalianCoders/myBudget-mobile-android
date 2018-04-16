package it.italiancoders.mybudget.fragment.main.movements

import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.movement.DayMovement
import it.italiancoders.mybudget.adapter.movement.MovementAdapter
import it.italiancoders.mybudget.fragment.movement.search.SearchMovementsFragment_
import it.italiancoders.mybudget.fragment.movement.search.params.SearchMovementsParams
import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.utils.DataUtils
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 30/03/18
 */
@EViewGroup(R.layout.view_dashboard_last_movement)
open class AccountDetailsLastMovementsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    @ViewById
    internal lateinit var movementsRecyclerView: RecyclerView

    fun updateView() {
        val movements = buildMovement()

        val adapter = MovementAdapter(movements)
        adapter.serverListSize = movements.size
        movementsRecyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        movementsRecyclerView.layoutManager = linearLayoutManager
    }

    private fun buildMovement(): List<Movement> {
        var currentDay = ""

        val movements = mutableListOf<Movement>()
        Config.currentAccount?.lastMovements.orEmpty().forEach {
            val movDay = DataUtils.formatLocalDate(it.executedAt ?: 0L)
            if (currentDay != movDay) {
                currentDay = movDay
                val dayMovement = DayMovement(it.executedAt ?: 0L)
                movements.add(dayMovement)
            }
            movements.add(it)
        }
        return movements.toList()
    }

    @Click
    internal fun goToSearchButtonClicked() {

        val calendar = Calendar.getInstance()
        val params = SearchMovementsParams(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1)

        FragmentUtils.replace(context as Activity?,
                SearchMovementsFragment_.builder().params(params).build(),
                animationType = FragmentUtils.AnimationType.SLIDE)
    }
}