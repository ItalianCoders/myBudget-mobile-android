/*
 * Project: myBudget-mobile-android
 * File: GoogleSignInAccountView.kt
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

package it.italiancoders.mybudget.activity.signin.google

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.squareup.picasso.Picasso
import it.italiancoders.mybudget.R
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/03/18
 */
@EViewGroup(R.layout.view_google_signin_account)
open class GoogleSignInAccountView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @ViewById
    lateinit var accountImage: ImageView

    @ViewById
    lateinit var idTV: TextView

    @ViewById
    lateinit var displayNameTV: TextView

    @ViewById
    lateinit var fullNameTV: TextView

    @ViewById
    lateinit var emailTV: TextView

    open fun bind(account: GoogleSignInAccount?) {
        idTV.visibility = View.GONE
        displayNameTV.visibility = View.GONE
        fullNameTV.visibility = View.GONE
        emailTV.visibility = View.GONE
        accountImage.visibility = View.GONE

        if (account != null) {
            //user display name
            val personName = account.displayName

            //user first name
            val personGivenName = account.givenName

            //user last name
            val personFamilyName = account.familyName

            //user email id
            val personEmail = account.email

            //user unique id
            val personId = account.id

            //user profile pic
            val personPhoto = account.photoUrl

            idTV.text = resources.getString(R.string.id, personId.orEmpty())
            displayNameTV.text = personName.orEmpty()
            val fullName = "${personGivenName.orEmpty()} ${personFamilyName.orEmpty()}"
            fullNameTV.text = fullName
            emailTV.text = personEmail.orEmpty()
            Picasso.get().load(personPhoto).fit().placeholder(R.mipmap.ic_launcher_round).into(accountImage)

            idTV.visibility = View.VISIBLE
            displayNameTV.visibility = View.VISIBLE
            fullNameTV.visibility = View.VISIBLE
            emailTV.visibility = View.VISIBLE
            accountImage.visibility = View.VISIBLE
        }
    }
}