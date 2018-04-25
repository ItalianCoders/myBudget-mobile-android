/*
 * Project: myBudget-mobile-android
 * File: User.kt
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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import java.io.Serializable


@JsonRootName(value = "user")
class User : Serializable {

    @JsonProperty("username")
    lateinit var username: String

    @JsonProperty(value = "password")
    var password: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("email")
    val email: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("firstname")
    val firstname: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("lastname")
    val lastname: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("alias")
    var alias: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("gender")
    val gender: GenderEnum? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("socialType")
    val socialType: SocialTypeEnum? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("profileImageUrl")
    val profileImageUrl: String? = null

    override fun toString(): String {
        return "User(username='$username', password=$password, email=$email, firstname=$firstname, lastname=$lastname, alias=$alias, gender=$gender, socialType=$socialType)"
    }


}
