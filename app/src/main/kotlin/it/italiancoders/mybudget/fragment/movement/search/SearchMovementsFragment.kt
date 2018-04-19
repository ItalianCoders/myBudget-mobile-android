package it.italiancoders.mybudget.fragment.movement.search

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.recycler.EndlessRecyclerViewScrollListener
import it.italiancoders.mybudget.adapter.movement.DayMovement
import it.italiancoders.mybudget.adapter.movement.MovementAdapter
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.fragment.movement.search.params.SearchMovementsParams
import it.italiancoders.mybudget.fragment.movement.search.params.SearchMovementsParamsView
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.rest.model.Page
import it.italiancoders.mybudget.utils.DataUtils
import it.italiancoders.mybudget.utils.FragmentUtils
import it.italiancoders.mybudget.utils.Utils
import org.androidannotations.annotations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author fattazzo
 *         <p/>
 *         date: 06/04/18
 */
@EFragment(R.layout.fragment_search_movements)
open class SearchMovementsFragment : BaseFragment() {

    @JvmField
    @FragmentArg
    @InstanceState
    var params: SearchMovementsParams? = null

    @ViewById
    internal lateinit var slidingLayout: SlidingUpPanelLayout

    @ViewById
    internal lateinit var resultMovementsRecyclerView: RecyclerView

    @ViewById
    internal lateinit var searchMovementsParamsView: SearchMovementsParamsView

    @ViewById
    internal lateinit var noResultTV: TextView

    @JvmField
    @InstanceState
    internal var currentPage = 0

    @JvmField
    @InstanceState
    internal var totalItems = -1

    @JvmField
    @InstanceState
    internal var allMovements = arrayListOf<Movement>()

    private var adapter: MovementAdapter? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    @AfterViews
    internal fun initViews() {
        searchMovementsParamsView.searchMovementsParams = params

        if (Config.currentAccountNeedReload) {
            resetData()
        }

        adapter = MovementAdapter(allMovements)
        adapter?.serverListSize = totalItems
        resultMovementsRecyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        resultMovementsRecyclerView.layoutManager = linearLayoutManager

        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadMovements()
            }
        }

        resultMovementsRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)

        if (params != null && allMovements.isEmpty()) {
            loadMovements()
        } else {
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
    }

    private fun loadMovements() {
        val userSearch = if (params!!.user == null) null else params!!.user!!.username
        RestClient.movementService.search(Config.currentAccount?.id.orEmpty(), params!!.anno, params!!.mese,
                params!!.giorno, userSearch, params!!.category?.id, currentPage).enqueue(object : Callback<Page<Movement>> {
            override fun onResponse(call: Call<Page<Movement>>, response: Response<Page<Movement>>) {
                if (response.isSuccessful) {
                    currentPage++
                    totalItems = response.body()?.totalElements?.toInt() ?: 0
                    adapter?.serverListSize = totalItems

                    val curSize = adapter?.itemCount ?: 0
                    val currentDay = if (allMovements.isEmpty()) "" else DataUtils.formatLocalDate(allMovements.last().executedAt
                            ?: 0L)
                    allMovements.addAll(buildMovement(currentDay, response.body()?.content.orEmpty()))

                    view?.post({
                        adapter?.notifyItemRangeInserted(curSize, allMovements.size)
                        noResultTV.visibility = if (allMovements.isEmpty()) View.VISIBLE else View.GONE
                    })
                } else {
                    Toast.makeText(activity, resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Page<Movement>>?, t: Throwable?) {
                Toast.makeText(activity, resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
            }

            private fun buildMovement(currentDayParam: String, originalMovement: List<Movement>): List<Movement> {
                var currentDay = currentDayParam

                val movements = mutableListOf<Movement>()
                originalMovement.forEach {
                    val movDay = DataUtils.formatLocalDate(it.executedAt ?: 0L)
                    if (currentDay != movDay) {
                        currentDay = movDay
                        val dayMovement = DayMovement(it.executedAt
                                ?: 0L)
                        movements.add(dayMovement)
                    }
                    movements.add(it)
                }
                return movements.toList()
            }
        })
    }

    @Click
    internal fun searchButtonClicked() {
        if (searchMovementsParamsView.searchMovementsParams != null) {
            Utils.hideSoftKeyboard(activity)
            params = searchMovementsParamsView.searchMovementsParams
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            endlessRecyclerViewScrollListener?.resetState()
            resetData()
            adapter?.serverListSize = -1
            adapter?.notifyDataSetChanged()

            loadMovements()
        }
    }

    private fun resetData() {
        currentPage = 0
        allMovements.clear()
        totalItems = -1
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity, MainFragment_.builder().build(), animationType = FragmentUtils.AnimationType.SLIDE)
        return true
    }

    override fun getActionBarTitleResId(): Int {
        return R.string.fragment_title_search_movements
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }
}