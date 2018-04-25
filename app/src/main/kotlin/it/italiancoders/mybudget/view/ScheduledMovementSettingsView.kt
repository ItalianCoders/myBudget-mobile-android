/*
 * Project: myBudget-mobile-android
 * File: ScheduledMovementSettingsView.kt
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

package it.italiancoders.mybudget.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.rest.model.ScheduledMovementSettings
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import org.androidannotations.annotations.res.StringArrayRes

/**
 * @author fattazzo
 *         <p/>
 *         date: 24/04/18
 */
@EViewGroup(R.layout.item_scheduled_movement_settings)
open class ScheduledMovementSettingsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<ScheduledMovementSettings> {

    @Bean
    lateinit var dataUtils: DataUtils

    @ViewById
    internal lateinit var nameTV: TextView

    @ViewById
    internal lateinit var descriptionTV: TextView

    @ViewById
    internal lateinit var startDateTV: TextView

    @ViewById
    internal lateinit var endDateTV: TextView

    @ViewById
    internal lateinit var frequencyTV: TextView

    @ViewById
    internal lateinit var categoryTV: TextView

    @ViewById
    internal lateinit var amountTV: TextView

    @ViewById
    internal lateinit var userTV: TextView

    @StringArrayRes(R.array.scheduled_frequency)
    internal lateinit var frequencyArrayRes: Array<String>

    override fun bind(objectToBind: ScheduledMovementSettings) {

        nameTV.text = objectToBind.name
        when (objectToBind.type) {
            MovementType.Incoming -> nameTV.setBackgroundColor(ContextCompat.getColor(context, R.color.primaryColor))
            MovementType.Expense -> nameTV.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
            else -> nameTV.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
        }

        descriptionTV.text = objectToBind.description.orEmpty()
        descriptionTV.visibility = if (objectToBind.description.isNullOrBlank()) View.GONE else View.VISIBLE

        startDateTV.text = DataUtils.formatLocalDate(objectToBind.start ?: 0)
        endDateTV.text = if (objectToBind.end != null) DataUtils.formatLocalDate(objectToBind.end!!) else "-"

        frequencyTV.text = frequencyArrayRes[objectToBind.frequency!!.value]

        categoryTV.text = objectToBind.category?.value.orEmpty()
        categoryTV.setCompoundDrawablesWithIntrinsicBounds(null, null, dataUtils.getCategoryImage(objectToBind.category!!), null)

        val amount = (objectToBind.amount
                ?: 0.0) * if (objectToBind.type ?: MovementType.Incoming == MovementType.Incoming) 1 else -1
        amountTV.text = dataUtils.formatCurrency(amount, showSign = true)

        userTV.text = objectToBind.user?.alias.orEmpty()
    }
}