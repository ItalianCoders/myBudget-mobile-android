package it.italiancoders.mybudget.activity.signin.facebook.entities

import android.net.Uri

class User(val picture: Uri, val name: String,
           val id: String, val email: String?, val permissions: String)
