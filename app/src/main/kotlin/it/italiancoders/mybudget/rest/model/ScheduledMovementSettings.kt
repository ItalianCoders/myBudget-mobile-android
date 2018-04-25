/*
 * Project: myBudget-mobile-android
 * File: ScheduledMovementSettings.kt
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

package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


/**
 * @author fattazzo
 *         <p/>
 *         date: 23/04/18
 */
class ScheduledMovementSettings : Serializable {

    @JsonProperty("id")
    var id: String = ""

    var name: String = ""

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("description")
    var description: String? = null

    @JsonProperty("start")
    var start: Long? = null

    @JsonProperty("end")
    var end: Long? = null

    @JsonIgnore
    val movementDate: Long? = null

    @JsonProperty("frequency")
    var frequency: ScheduledFrequencyEnum? = null

    @JsonProperty("account")
    var account: Account? = null

    @JsonProperty("amount")
    var amount: Double? = null

    @JsonProperty("category")
    var category: Category? = null

    @JsonProperty("user")
    var user: User? = null

    @JsonProperty("movementType")
    var type: MovementType? = null

    fun isValid(): Boolean {
        return name.isNotBlank() && start != null && frequency != null &&
                account != null && amount != null && category != null && user != null &&
                type != null
    }
}