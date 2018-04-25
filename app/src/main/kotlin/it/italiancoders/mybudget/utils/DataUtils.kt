/*
 * Project: myBudget-mobile-android
 * File: DataUtils.kt
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

package it.italiancoders.mybudget.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.preferences.ApplicationPreferenceManager
import it.italiancoders.mybudget.rest.model.Category
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 31/03/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class DataUtils {

    @Bean
    internal lateinit var preferenceManager: ApplicationPreferenceManager

    @RootContext
    internal lateinit var context: Context

    /**
     * Format number to currency whit symbol configured in app preferences.
     *
     * @return formatted number
     */
    fun formatCurrency(value: Double, showSign: Boolean = false, sign: String? = null): String {
        val signPos = sign ?: "+"
        val signNeg = sign ?: "-"

        var pattern = "#,##0.00${preferenceManager.getCurrency().symbol}"
        if (showSign) {
            pattern = "$signPos#,##0.00${preferenceManager.getCurrency().symbol};$signNeg#,##0.00${preferenceManager.getCurrency().symbol}"
        }
        val decimalFormat = DecimalFormat(pattern)

        return decimalFormat.format(value)
    }

    fun getCategoryImage(category: Category?): Drawable {
        val defaultImage = ContextCompat.getDrawable(context, R.drawable.category)!!
        return try {
            val icons = context.resources.obtainTypedArray(R.array.cateogory_icons)
            val img = if (category?.iconId != null) icons.getDrawable(category.iconId!!) else defaultImage
            icons.recycle()
            img
        } catch (e: Exception) {
            defaultImage
        }
    }


    companion object {

        /**
         * Format UTC milliseconds to local date.
         *
         * @param utcMillisec milliseconds
         * @param pattern format pattern
         *
         * @return formatted date
         */
        fun formatLocalDate(utcMillisec: Long, pattern: String = "dd MMMM yyyy"): String {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.timeZone = Calendar.getInstance().timeZone

            return try {
                sdf.format(Date(utcMillisec * 1000))
            } catch (e: Exception) {
                ""
            }
        }

        fun toUnixDate(date: Date?): Long? {
            return if (date == null) {
                null
            } else {
                date.time / 1000
            }
        }
    }
}