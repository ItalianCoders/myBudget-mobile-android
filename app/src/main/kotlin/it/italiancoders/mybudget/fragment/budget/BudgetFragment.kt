/*
 * Project: myBudget-mobile-android
 * File: BudgetFragment.kt
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

import android.support.design.widget.TextInputEditText
import android.view.Menu
import android.view.MenuInflater
import android.widget.CheckBox
import android.widget.Spinner
import com.afollestad.materialdialogs.MaterialDialog
import com.xw.repo.BubbleSeekBar
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.CategoryAdapter
import it.italiancoders.mybudget.adapter.ScheduledFrequencyAdapter
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.BudgetManager
import it.italiancoders.mybudget.rest.model.Budget
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.ScheduledFrequencyEnum
import org.androidannotations.annotations.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 08/05/18
 */
@EFragment(R.layout.fragment_budget)
open class BudgetFragment : BaseFragment() {

    @Bean
    lateinit var budgetManager: BudgetManager

    @ViewById
    internal lateinit var nameTIET: TextInputEditText

    @ViewById
    internal lateinit var descriptionTIET: TextInputEditText

    @ViewById
    internal lateinit var amountTIET: TextInputEditText

    @ViewById
    internal lateinit var categorySpinner: Spinner

    @ViewById
    internal lateinit var frequencySpinner: Spinner

    @ViewById
    internal lateinit var alertThresholdSeekBar: BubbleSeekBar

    @ViewById
    internal lateinit var alertPushNotificationCheckBox: CheckBox

    @ViewById
    internal lateinit var alertMailCheckBox: CheckBox

    @JvmField
    @InstanceState
    @FragmentArg
    var budget: Budget = Budget()

    private val categories: List<Category> by lazy {
        val catTmp = mutableListOf<Category>()
        catTmp.addAll(Config.currentAccount?.incomingCategoriesAvailable.orEmpty())
        catTmp.addAll(Config.currentAccount?.expenseCategoriesAvailable.orEmpty())

        val accountCategories = catTmp.toList()
        accountCategories.sortedWith(kotlin.Comparator { cat1, cat2 -> cat1.value.orEmpty().compareTo(cat2.value.orEmpty()) })
        accountCategories
    }


    @AfterViews
    fun initView() {
        nameTIET.settext(budget.name)
        descriptionTIET.settext(budget.description.orEmpty())
        amountTIET.setText(budget.amount.toString())

        val allCategory = Category()
        allCategory.value = context!!.getString(R.string.all_f)

        val categoryAdapter = CategoryAdapter(activity!!, categories, allCategory)
        categorySpinner.adapter = categoryAdapter
        categorySpinner.post {
            categorySpinner.setSelection(if (budget.category == null || categories.indexOf(budget.category!!) == -1)
                0
            else (categories.indexOf(budget.category!!)) + 1)
        }

        frequencySpinner.adapter = ScheduledFrequencyAdapter(context!!)
        frequencySpinner.setSelection(budget.frequency.value)

        alertThresholdSeekBar.setProgress(budget.alertThreshold?.toFloat() ?: 0f)

        alertPushNotificationCheckBox.isChecked = budget.isEnableAlertPushNotification
        alertMailCheckBox.isChecked = budget.isEnableAlertMail
    }

    @OptionsItem
    fun menuBudgetSaveSelected() {
        syncModelViewView()

        nameTIET.updateError(budget.name.isBlank(), R.string.required)
        amountTIET.updateError(budget.amount == 0.0, R.string.required)

        if(budget.name.isNotBlank() && budget.amount != 0.0) {
            if (budget.id.isNullOrBlank()) {
                insertBudget()
            } else {
                updateBudget()
            }
        }
    }

    private fun insertBudget() {
        budgetManager.insert(context!!, Config.currentAccount?.id!!, budget, object : Closure<Void> {
            override fun onSuccess() {
                backPressed()
            }
        })
    }

    private fun updateBudget() {
        budgetManager.edit(context!!, Config.currentAccount?.id!!, budget, object : Closure<Void> {
            override fun onSuccess() {
                backPressed()
            }
        })
    }

    @OptionsItem
    fun menuBudgetDeleteSelected() {
        MaterialDialog.Builder(context!!)
                .iconRes(R.drawable.delete)
                .title(R.string.app_name)
                .content(R.string.remove_budget_dialog_text)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ ->
                    budgetManager.delete(context!!, Config.currentAccount?.id.orEmpty(), budget.id!!, object : Closure<Void> {
                        override fun onSuccess() {
                            backPressed()
                        }
                    })
                }
                .show()
    }

    private fun syncModelViewView() {
        budget.name = nameTIET.text.toString()
        budget.description = descriptionTIET.text.toString()

        try {
            budget.amount = amountTIET.text.toString().toDouble()
        } catch (e: Exception) {
            budget.amount = 0.0
        }
        budget.category = categorySpinner.selectedItem as Category
        budget.frequency = frequencySpinner.selectedItem as ScheduledFrequencyEnum

        budget.alertThreshold = alertThresholdSeekBar.progress.toDouble()
        budget.isEnableAlertPushNotification = alertPushNotificationCheckBox.isChecked
        budget.isEnableAlertMail = alertMailCheckBox.isChecked

        budget.account = Config.currentAccount!!
    }

    override fun getActionBarTitleResId(): Int {
        return if (budget.id.isNullOrBlank()) R.string.fragment_budget_new else R.string.fragment_budget_edit
    }

    override fun backPressed(): Boolean {
        activity?.supportFragmentManager?.popBackStack()
        return true
    }

    // ---------- Menu -------------------
    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.budget, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        menu?.findItem(R.id.menuBudgetDelete)?.isVisible = budget.id.orEmpty().isNotBlank()
    }

    // ------------------------------------------- custom func

    private fun TextInputEditText.updateError(show: Boolean, errorResId: Int?, formatArg: Any? = null) {
        error = if (show) {
            getString(errorResId!!, formatArg)
        } else {
            null
        }
    }

    private fun TextInputEditText.settext(textString: String) {
        text.clear()
        text.append(textString)
    }
}