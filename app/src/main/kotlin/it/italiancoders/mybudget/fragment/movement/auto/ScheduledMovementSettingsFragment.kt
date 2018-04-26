/*
 * Project: myBudget-mobile-android
 * File: ScheduledMovementSettingsFragment.kt
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

import android.app.DatePickerDialog
import android.support.design.widget.TextInputEditText
import android.view.Menu
import android.view.MenuInflater
import android.widget.Spinner
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.CategoryAdapter
import it.italiancoders.mybudget.adapter.MovementTypeAdapter
import it.italiancoders.mybudget.adapter.ScheduledFrequencyAdapter
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.ScheduledMovementsManager
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.rest.model.ScheduledFrequencyEnum
import it.italiancoders.mybudget.rest.model.ScheduledMovementSettings
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/04/18
 */
@EFragment(R.layout.fragment_scheduled_movement_settings)
open class ScheduledMovementSettingsFragment : BaseFragment() {

    @Bean
    lateinit var scheduledMovementsManager: ScheduledMovementsManager

    @ViewById
    internal lateinit var nameTIET: TextInputEditText

    @ViewById
    internal lateinit var descriptionTIET: TextInputEditText

    @ViewById
    internal lateinit var startDateTIET: TextInputEditText
    @ViewById
    internal lateinit var endDateTIET: TextInputEditText

    @ViewById
    internal lateinit var frequencySpinner: Spinner

    @ViewById
    internal lateinit var typeSpinner: Spinner

    @ViewById
    internal lateinit var categorySpinner: Spinner

    @ViewById
    internal lateinit var amountTIET: TextInputEditText

    @JvmField
    @InstanceState
    @FragmentArg
    var scheduledMovementSettings: ScheduledMovementSettings = ScheduledMovementSettings()

    @AfterViews
    fun initView() {
        nameTIET.settext(scheduledMovementSettings.name)
        descriptionTIET.settext(scheduledMovementSettings.description.orEmpty())
        startDateTIET.settext(if (scheduledMovementSettings.start != null)
            DataUtils.formatLocalDate(scheduledMovementSettings.start
                    ?: 0, pattern = "dd/MM/yyyy")
        else "")
        endDateTIET.settext(if (scheduledMovementSettings.end != null)
            DataUtils.formatLocalDate(scheduledMovementSettings.end
                    ?: 0, pattern = "dd/MM/yyyy")
        else "")

        frequencySpinner.adapter = ScheduledFrequencyAdapter(context!!)
        frequencySpinner.setSelection(scheduledMovementSettings.frequency?.value ?: 0)

        typeSpinner.adapter = MovementTypeAdapter(context!!)
        typeSpinner.setSelection(scheduledMovementSettings.type?.value ?: 0)

        amountTIET.setText(scheduledMovementSettings.amount?.toString() ?: "")


    }

    @OptionsItem
    fun menuScheduledMovementSettingsSaveSelected() {
        syncModelViewView()

        nameTIET.updateError(scheduledMovementSettings.name.isBlank(), R.string.required)
        startDateTIET.updateError(scheduledMovementSettings.start == null, R.string.required_date, getString(R.string.date_format_dd_mm_yyyyy))
        amountTIET.updateError(scheduledMovementSettings.amount == null, R.string.required)

        if (scheduledMovementSettings.isValid()) {
            if (scheduledMovementSettings.id.isBlank()) {
                saveSettings()
            } else {
                updateSettings()
            }
        }
    }

    @OptionsItem
    fun menuScheduledMovementSettingsDeleteSelected() {
        MaterialDialog.Builder(context!!)
                .iconRes(R.drawable.delete)
                .title(R.string.app_name)
                .content(R.string.remove_scheduled_movement_dialog_text)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ ->
                    scheduledMovementsManager.delete(context!!, scheduledMovementSettings.id, object : Closure<Void> {
                        override fun onSuccess() {
                            backPressed()
                        }
                    })
                }
                .show()
    }

    private fun saveSettings() {
        scheduledMovementsManager.save(context!!, scheduledMovementSettings, object : Closure<Void> {
            override fun onSuccess() {
                backPressed()
            }
        })
    }

    private fun updateSettings() {
        scheduledMovementsManager.update(context!!, scheduledMovementSettings, object : Closure<Void> {
            override fun onSuccess() {
                backPressed()
            }
        })
    }

    @Click
    fun startDateButtonClicked() {
        val newCalendar = Calendar.getInstance()

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            startDateTIET.settext("$dayOfMonth/${month + 1}/$year")
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    @Click
    fun endDateButtonClicked() {
        val newCalendar = Calendar.getInstance()

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            startDateTIET.settext("$dayOfMonth/${month + 1}/$year")
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    @ItemSelect
    fun typeSpinnerItemSelected(selected: Boolean, type: MovementType) {
        val categories = getCategoriesForCurrentMovementType(type)
        categorySpinner.adapter = CategoryAdapter(context!!, categories)
        categorySpinner.post {
            categorySpinner.setSelection(if (scheduledMovementSettings.category == null || categories.indexOf(scheduledMovementSettings.category!!) == -1)
                0
            else categories.indexOf(scheduledMovementSettings.category!!))
        }
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.scheduled_movement_settings, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        menu?.findItem(R.id.menuScheduledMovementSettingsDelete)?.isVisible = scheduledMovementSettings.id.isNotBlank()
    }

    private fun syncModelViewView() {
        scheduledMovementSettings.name = nameTIET.text.toString()
        scheduledMovementSettings.description = descriptionTIET.text.toString()

        val startDateString = startDateTIET.text.toString()
        scheduledMovementSettings.start = try {
            val startDate = SimpleDateFormat("dd/M/yyyy", Config.locale).parse(startDateString)
            DataUtils.toUnixDate(startDate)
        } catch (e: Exception) {
            null
        }

        val endDateString = endDateTIET.text.toString()
        scheduledMovementSettings.end = try {
            val endDate = SimpleDateFormat("dd/M/yyyy", Config.locale).parse(endDateString)
            DataUtils.toUnixDate(endDate)
        } catch (e: Exception) {
            null
        }

        scheduledMovementSettings.frequency = frequencySpinner.selectedItem as ScheduledFrequencyEnum
        scheduledMovementSettings.type = typeSpinner.selectedItem as MovementType
        scheduledMovementSettings.category = categorySpinner.selectedItem as Category

        try {
            scheduledMovementSettings.amount = amountTIET.text.toString().toDouble()
        } catch (e: Exception) {
            scheduledMovementSettings.amount = null
        }
        scheduledMovementSettings.account = Config.currentAccount
        scheduledMovementSettings.user = Config.user
    }

    private fun getCategoriesForCurrentMovementType(type: MovementType): List<Category> {

        return when (type) {
            MovementType.Incoming -> Config.currentAccount?.incomingCategoriesAvailable.orEmpty()
            MovementType.Expense -> Config.currentAccount?.expenseCategoriesAvailable.orEmpty()
            MovementType.Both -> {
                val allCategories = arrayListOf<Category>()
                allCategories.addAll(Config.currentAccount?.incomingCategoriesAvailable.orEmpty())
                allCategories.addAll(Config.currentAccount?.expenseCategoriesAvailable.orEmpty())
                allCategories.toList()
            }
        }
    }

    override fun backPressed(): Boolean {
        activity?.supportFragmentManager?.popBackStack()
        return true
    }

    override fun getActionBarTitleResId(): Int {
        return if (scheduledMovementSettings.id.isBlank()) R.string.fragment_sheduled_movement_new else R.string.fragment_sheduled_movement_edit
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