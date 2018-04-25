/*
 * Project: myBudget-mobile-android
 * File: CategoryView.kt
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/04/18
 */
@EViewGroup(R.layout.item_category)
open class CategoryView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<Category> {

    @ViewById
    internal lateinit var valueTV: TextView

    @ViewById
    internal lateinit var iconImage: ImageView

    @Bean
    internal lateinit var dataUtils: DataUtils

    override fun bind(objectToBind: Category) {
        valueTV.text = objectToBind.value.orEmpty()

        iconImage.setImageDrawable(dataUtils.getCategoryImage(objectToBind))
        val color = when (objectToBind.type) {
            MovementType.Expense -> R.color.errorTextColor
            MovementType.Incoming -> R.color.primaryDarkColor
            else -> android.R.color.black
        }
        iconImage.imageTintList = ContextCompat.getColorStateList(context, color)
    }
}