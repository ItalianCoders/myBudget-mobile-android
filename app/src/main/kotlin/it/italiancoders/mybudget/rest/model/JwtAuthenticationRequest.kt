/*
 * Project: myBudget-mobile-android
 * File: JwtAuthenticationRequest.kt
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
import java.io.Serializable

class JwtAuthenticationRequest()  : Serializable {

    @JsonProperty("username")
    var username: String? = null
        private set

    @JsonProperty("password")
    var password: String? = null
        private set

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("socialAuthenticationType")
    var socialAuthenticationType: SocialTypeEnum? = null
        private set

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("socialAccessToken")
    var socialAccessToken: String? = null
        private set

    constructor(username: String, password: String, socialAuthenticationType: SocialTypeEnum, socialAccessToken: String) : this() {
        this.username = username
        this.password = password
        this.socialAuthenticationType = socialAuthenticationType
        this.socialAccessToken = socialAccessToken
    }
}
