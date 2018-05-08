/*
 * Project: myBudget-mobile-android
 * File: BudgetView.kt
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
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.Budget
import it.italiancoders.mybudget.utils.DataUtils
import me.itangqi.waveloadingview.WaveLoadingView
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import org.androidannotations.annotations.res.StringArrayRes

/**
 * @author fattazzo
 *         <p/>
 *         date: 07/05/18
 */
@EViewGroup(R.layout.item_budget)
open class BudgetView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<Budget> {

    @Bean
    lateinit var dataUtils: DataUtils

    @ViewById
    internal lateinit var nameTV: TextView

    @ViewById
    internal lateinit var descriptionTV: TextView

    @ViewById
    internal lateinit var amountTV: TextView

    @ViewById
    internal lateinit var frequencyTV: TextView

    @ViewById
    internal lateinit var categoryTV: TextView

    @ViewById
    internal lateinit var waveLoadingView: WaveLoadingView

    @StringArrayRes(R.array.scheduled_frequency)
    internal lateinit var frequencyArrayRes: Array<String>

    override fun bind(objectToBind: Budget) {

        nameTV.text = objectToBind.name

        descriptionTV.text = objectToBind.description.orEmpty()

        amountTV.text = dataUtils.formatCurrency(objectToBind.amount)

        frequencyTV.text = frequencyArrayRes[objectToBind.frequency.value]

        categoryTV.text = objectToBind.category?.value ?: resources.getString(R.string.all_categories)
        categoryTV.setCompoundDrawablesWithIntrinsicBounds(dataUtils.getCategoryImage(objectToBind.category), null, null, null)

        waveLoadingView.centerTitle = dataUtils.formatCurrency(objectToBind.amountSpent)

        val spentPercent = objectToBind.amountSpent * 100 / objectToBind.amount
        val waveColor = when {
            spentPercent <= 30.0 -> R.color.primaryColor
            spentPercent <= 70.0 -> android.R.color.holo_orange_dark
            else -> android.R.color.holo_red_dark
        }
        waveLoadingView.progressValue = 100 - spentPercent.toInt()
        waveLoadingView.waveColor = ContextCompat.getColor(context, waveColor)
    }
}