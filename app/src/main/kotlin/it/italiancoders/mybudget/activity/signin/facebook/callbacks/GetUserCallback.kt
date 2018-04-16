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