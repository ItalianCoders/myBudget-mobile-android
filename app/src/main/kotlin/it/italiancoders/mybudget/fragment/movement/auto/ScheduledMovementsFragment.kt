/*
 * Project: myBudget-mobile-android
 * File: ScheduledMovementsFragment.kt
 *
 * Created by fattazzo
 * Copyright © 2018 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.fragment.movement.auto

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.ScheduledMovementAdapter
import it.italiancoders.mybudget.adapter.base.recycler.RecyclerViewAdapterBase
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.ScheduledMovementsManager
import it.italiancoders.mybudget.rest.model.ScheduledMovementSettings
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/04/18
 */
@EFragment(R.layout.fragment_scheduled_movements)
open class ScheduledMovementsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapterBase.OnItemClickListener<ScheduledMovementSettings> {

    @ViewById
    internal lateinit var noMovementsTV: TextView

    @ViewById
    internal lateinit var scheduledMovementsRecyclerView: RecyclerView

    @ViewById
    internal lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Bean
    lateinit var scheduledMovementsManager: ScheduledMovementsManager

    @JvmField
    @InstanceState
    internal var scheduledMovements: ArrayList<ScheduledMovementSettings> = arrayListOf()

    @AfterViews
    fun initViews() {
        swipeRefreshLayout.setOnRefreshListener(this)

        if (scheduledMovements.isEmpty()) {
            onRefresh()
        } else {
            updateViews()
        }
    }

    override fun onRefresh() {
        scheduledMovements.clear()
        scheduledMovementsManager.loadAll(context!!, object : Closure<List<ScheduledMovementSettings>> {
            override fun onSuccess(result: List<ScheduledMovementSettings>) {
                scheduledMovements.addAll(result)
                updateViews()
            }

            override fun onError() {
                updateViews()
            }

            override fun onFailure() {
                updateViews()
            }
        })
    }

    private fun updateViews() {
        swipeRefreshLayout.isRefreshing = false

        noMovementsTV.visibility = if (scheduledMovements.isEmpty()) View.VISIBLE else View.GONE

        val adapter = ScheduledMovementAdapter(context!!, this)

        adapter.items = scheduledMovements

        scheduledMovementsRecyclerView.adapter = adapter
        scheduledMovementsRecyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.scheduled_movements_columns))
    }

    override fun onItemClick(item: ScheduledMovementSettings?) {
        openSettingsFragment(item)
    }

    @OptionsItem
    fun menuScheduledMovementsAddSelected() {
        openSettingsFragment()
    }

    private fun openSettingsFragment(settings: ScheduledMovementSettings? = null) {
        FragmentUtils.replace(activity, ScheduledMovementSettingsFragment_.builder().scheduledMovementSettings(settings
                ?: ScheduledMovementSettings()).build())
        scheduledMovements.clear()
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.scheduled_movements, menu)
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity, MainFragment_.builder().build())
        return true
    }

    override fun getActionBarTitleResId(): Int {
        return R.string.fragment_sheduled_movements
    }
}