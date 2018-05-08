/*
 * Project: myBudget-mobile-android
 * File: BudgetService.kt
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

package it.italiancoders.mybudget.rest.api

import it.italiancoders.mybudget.rest.model.Budget
import retrofit2.Call
import retrofit2.http.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 07/05/18
 */
interface BudgetService {

    @GET("/protected/v1/accounts/{accountId}/budgets")
    fun loadAll(@Path("accountId") accountId: String): Call<List<Budget>>

    @DELETE("/protected/v1/accounts/{accountId}/budgets/{budgetId}")
    fun delete(@Path("accountId") accountId: String, @Path("budgetId") budgetId: String): Call<Void>

    @PUT("/protected/v1/accounts/{accountId}/budgets/{budgetId}")
    fun edit(@Path("accountId") accountId: String,
             @Path("budgetId") budgetId: String,
             @Body budget: Budget): Call<Void>

    @POST("/protected/v1/accounts/{accountId}/budgets/{budgetId}")
    fun insert(@Path("accountId") accountId: String,
               @Body budget: Budget): Call<Void>
}