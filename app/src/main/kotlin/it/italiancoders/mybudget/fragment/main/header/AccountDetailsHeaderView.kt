/*
 * Project: myBudget-mobile-android
 * File: AccountDetailsHeaderView.kt
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

package it.italiancoders.mybudget.fragment.main.header

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/03/18
 */
@EViewGroup(R.layout.view_dashboard_header)
open class AccountDetailsHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Bean
    internal lateinit var dataUtils: DataUtils

    @ViewById
    internal lateinit var incTotalContentTV: TextView

    @ViewById
    internal lateinit var expTotalContentTV: TextView

    @ViewById
    internal lateinit var diffTotalContentTV: TextView

    fun updateView() {

        val accountDetails = Config.currentAccount

        val incTot = accountDetails?.totalMonthlyIncoming ?: 0.0
        val expTot = accountDetails?.totalMonthlyExpense ?: 0.0

        val incTotStr = dataUtils.formatCurrency(incTot)
        val expTotStr = dataUtils.formatCurrency(expTot)
        val difTotStr = dataUtils.formatCurrency(incTot - expTot,showSign = true)

        incTotalContentTV.text = incTotStr
        expTotalContentTV.text = expTotStr
        diffTotalContentTV.text = difTotStr
    }
}