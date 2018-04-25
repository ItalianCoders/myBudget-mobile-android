/*
 * Project: myBudget-mobile-android
 * File: MovementTypeAdapter.kt
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

package it.italiancoders.mybudget.adapter

import android.content.Context
import it.italiancoders.mybudget.adapter.base.BaseFilterAdapter
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.view.MovementTypeView_

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/04/18
 */
class MovementTypeAdapter(context: Context, extraItem: MovementType? = null) :
        BaseFilterAdapter<MovementType>(context, MovementType.values().asList(), extraItem) {

    override fun buidView(): BindableView<MovementType> = MovementTypeView_.build(context)
}