/*
 * Project: myBudget-mobile-android
 * File: GetUserCallback.kt
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

package it.italiancoders.mybudget.activity.signin.facebook.callbacks

import android.net.Uri
import com.facebook.GraphRequest
import it.italiancoders.mybudget.activity.signin.facebook.entities.User
import org.json.JSONException
import org.json.JSONObject

class GetUserCallback(private val mGetUserResponse: IGetUserResponse) {
    val callback: GraphRequest.Callback

    init {
        callback = GraphRequest.Callback { response ->
            var user: User? = null
            try {
                val userObj = response.jsonObject ?: return@Callback
                user = jsonToUser(userObj)

            } catch (e: JSONException) {
                // Handle exception ...
            }

            // Handled by ProfileActivity
            mGetUserResponse.onCompleted(user)
        }
    }

    @Throws(JSONException::class)
    private fun jsonToUser(user: JSONObject): User {
        val picture = Uri.parse(user.getJSONObject("picture").getJSONObject("data").getString("url"))
        val name = user.getString("name")
        val id = user.getString("id")
        var email: String? = null
        if (user.has("email")) {
            email = user.getString("email")
        }

        // Build permissions display string
        val builder = StringBuilder()
        val perms = user.getJSONObject("permissions").getJSONArray("data")
        builder.append("Permissions:\n")
        for (i in 0 until perms.length()) {
            builder.append(perms.getJSONObject(i).get("permission")).append(": ").append(perms
                    .getJSONObject(i).get("status")).append("\n")
        }
        val permissions = builder.toString()

        return User(picture, name, id, email, permissions)
    }

    interface IGetUserResponse {
        fun onCompleted(user: User?)
    }
}