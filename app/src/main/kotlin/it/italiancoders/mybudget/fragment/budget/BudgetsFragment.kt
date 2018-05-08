/*
 * Project: myBudget-mobile-android
 * File: BudgetsFragment.kt
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

package it.italiancoders.mybudget.fragment.budget

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.TextView
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.BudgetsAdapter
import it.italiancoders.mybudget.adapter.base.recycler.RecyclerViewAdapterBase
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.main.MainFragment_
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.BudgetManager
import it.italiancoders.mybudget.rest.model.Budget
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 07/05/18
 */
@EFragment(R.layout.fragment_budgets)
open class BudgetsFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapterBase.OnItemClickListener<Budget> {

    @ViewById
    internal lateinit var noBudgetsTV: TextView

    @ViewById
    internal lateinit var budgetsRecyclerView: RecyclerView

    @ViewById
    internal lateinit var swipeRefreshLayout: SwipeRefreshLayout

    @Bean
    lateinit var budgetManager: BudgetManager

    @JvmField
    @InstanceState
    internal var budgets: ArrayList<Budget> = arrayListOf()

    @AfterViews
    fun initViews() {
        swipeRefreshLayout.setOnRefreshListener(this)

        if (budgets.isEmpty()) {
            onRefresh()
        } else {
            updateViews()
        }
    }

    override fun onRefresh() {
        budgets.clear()
        budgetManager.loadAll(context!!, Config.currentAccount?.id.orEmpty(), object : Closure<List<Budget>> {
            override fun onSuccess(result: List<Budget>) {
                budgets.addAll(result)
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

        noBudgetsTV.visibility = if (budgets.isEmpty()) View.VISIBLE else View.GONE

        val adapter = BudgetsAdapter(context!!, this)

        adapter.items = budgets

        budgetsRecyclerView.adapter = adapter
        budgetsRecyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.scheduled_movements_columns))
    }

    override fun onItemClick(item: Budget?) {
        openBidgetFragment(item)
    }

    @OptionsItem
    fun menuBudgetsAddSelected() {
        openBidgetFragment()
    }

    private fun openBidgetFragment(budget: Budget? = null) {
        FragmentUtils.replace(activity, BudgetFragment_.builder().budget(budget
                ?: Budget()).build())
        budgets.clear()
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.budgets, menu)
    }

    override fun backPressed(): Boolean {
        FragmentUtils.replace(activity, MainFragment_.builder().build(), animationType = FragmentUtils.AnimationType.SLIDE)
        return true
    }

    override fun getActionBarTitleResId(): Int {
        return R.string.fragment_budgets
    }
}