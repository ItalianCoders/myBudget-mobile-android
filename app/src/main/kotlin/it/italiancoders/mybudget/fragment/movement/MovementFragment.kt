/*
 * Project: myBudget-mobile-android
 * File: MovementFragment.kt
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

package it.italiancoders.mybudget.fragment.movement

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.Menu
import android.view.MenuInflater
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.Config.DEFAULT_CATEGORY_EXPENSE_ID
import it.italiancoders.mybudget.Config.DEFAULT_CATEGORY_INCOMING_ID
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.CategoryAdapter
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 31/03/18
 */
@EFragment(R.layout.fragment_movement)
open class MovementFragment : BaseFragment() {

    @JvmField
    @InstanceState
    @FragmentArg
    internal var movementType: MovementType = MovementType.Both

    @JvmField
    @InstanceState
    @FragmentArg
    internal var movement = Movement()

    @ViewById
    internal lateinit var categorySpinner: Spinner
    @ViewById
    internal lateinit var amountTIET: TextInputEditText
    @ViewById
    internal lateinit var noteTIET: TextInputEditText

    private val categories: List<Category> by lazy {
        val accountCategories = when (movementType) {
            MovementType.Incoming -> Config.currentAccount?.incomingCategoriesAvailable.orEmpty()
            MovementType.Expense -> Config.currentAccount?.expenseCategoriesAvailable.orEmpty()
            MovementType.Both -> {
                val catTmp = mutableListOf<Category>()
                catTmp.addAll(Config.currentAccount?.incomingCategoriesAvailable.orEmpty())
                catTmp.addAll(Config.currentAccount?.expenseCategoriesAvailable.orEmpty())
                catTmp.toList()
            }
        }
        accountCategories.sortedWith(kotlin.Comparator { cat1, cat2 -> cat1.value.orEmpty().compareTo(cat2.value.orEmpty()) })

        (0 until accountCategories.size).forEach {
            if (accountCategories[it].id.equals(DEFAULT_CATEGORY_EXPENSE_ID) || accountCategories[it].id.equals(DEFAULT_CATEGORY_INCOMING_ID))
                Collections.swap(accountCategories, 0, it)
        }
        accountCategories
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @AfterViews
    internal fun initView() {
        movementType = movement.type ?: movementType
        movement.category = movement.category ?: categories.firstOrNull()

        val categoryAdapter = CategoryAdapter(context!!, categories)
        categorySpinner.adapter = categoryAdapter
        categorySpinner.setSelection(categories.indexOf(movement.category!!))

        amountTIET.setText(movement.amount?.toString() ?: "", TextView.BufferType.EDITABLE)
        noteTIET.setText(movement.note.orEmpty(), TextView.BufferType.EDITABLE)
    }

    @FocusChange
    fun amountTIETFocusChanged(view: TextInputEditText, hasFocus: Boolean) {
        if (!hasFocus && view.text.toString().isNotEmpty()) {
            try {
                movement.amount = view.text.toString().toDouble()
            } catch (e: Exception) {
                movement.amount = 0.0
            }
        }
    }

    @FocusChange
    fun noteTIETFocusChanged(view: TextInputEditText, hasFocus: Boolean) {
        if (!hasFocus && view.text.toString().isNotEmpty()) {
            movement.note = view.text.toString()
        }
    }

    @ItemSelect
    fun categorySpinnerItemSelected(selected: Boolean) {
        movement.category = categorySpinner.selectedItem as Category?
    }

    @OptionsItem
    internal fun menuMovementSaveSelected() {
        categorySpinner.requestFocusFromTouch()

        Config.currentAccountNeedReload = true
        if (movement.id == null) {
            movement.type = movementType
            movement.executedBy = Config.user
            movement.executedAt = DataUtils.toUnixDate(Calendar.getInstance().time)
            openIndeterminateDialog(R.string.saving_movement)
            RestClient.movementService.insert(Config.currentAccount?.id.orEmpty(), movement).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    closeIndeterminateDialog()
                    if (response.isSuccessful) {
                        backPressed()
                    } else {
                        Toast.makeText(activity, getString(R.string.saving_movement_error), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    closeIndeterminateDialog()
                    Toast.makeText(activity, getString(R.string.saving_movement_error), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            openIndeterminateDialog(R.string.saving_movement)
            RestClient.movementService.edit(Config.currentAccount?.id.orEmpty(), movement.id!!, movement).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    closeIndeterminateDialog()
                    if (response.isSuccessful) {
                        backPressed()
                    } else {
                        Toast.makeText(activity, resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    closeIndeterminateDialog()
                    Toast.makeText(activity, resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun backPressed(): Boolean {
        activity?.supportFragmentManager?.popBackStack()
        return true
    }

    override fun getActionBarTitle(): String? {
        val movTypeString = if ((movement.type
                        ?: movementType) == MovementType.Expense) getString(R.string.expense_singular) else getString(R.string.incoming_singular)

        return if (movement.id == null) {
            "${getString(R.string.new_f_action)} $movTypeString"
        } else {
            "${getString(R.string.edit_action)} $movTypeString"
        }
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.movement, menu)
    }
}