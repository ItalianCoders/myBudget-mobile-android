/*
 * Project: myBudget-mobile-android
 * File: FlexibleFrameLayout.kt
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
import android.util.AttributeSet
import android.widget.FrameLayout

class FlexibleFrameLayout : FrameLayout {

    private var currentOrder: Int = 0

    constructor(context: Context) : super(context) {
        isChildrenDrawingOrderEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        isChildrenDrawingOrderEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        isChildrenDrawingOrderEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        isChildrenDrawingOrderEnabled = true
    }

    fun setDrawOrder(order: Int) {
        currentOrder = order
        invalidate()
    }

    override fun getChildDrawingOrder(childCount: Int, i: Int): Int {
        return DRAW_ORDERS[currentOrder][i]
    }

    companion object {
        var ORDER_SIGN_UP_STATE = 0
        var ORDER_LOGIN_STATE = 1

        private val DRAW_ORDERS = arrayOf(intArrayOf(0, 1, 2), intArrayOf(2, 1, 0))
    }
}