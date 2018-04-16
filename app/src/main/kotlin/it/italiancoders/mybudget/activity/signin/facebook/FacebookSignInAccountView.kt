package it.italiancoders.mybudget.activity.signin.facebook

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.signin.facebook.entities.User
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/03/18
 */
@EViewGroup(R.layout.view_facebook_signin_account)
open class FacebookSignInAccountView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @ViewById
    lateinit var accountImage: ImageView

    @ViewById
    lateinit var idTV: TextView

    @ViewById
    lateinit var nameTV: TextView

    @ViewById
    lateinit var emailTV: TextView

    @ViewById
    lateinit var permissionsTV: TextView

    open fun bind(user: User?) {
        idTV.visibility = View.GONE
        nameTV.visibility = View.GONE
        emailTV.visibility = View.GONE
        permissionsTV.visibility = View.GONE
        accountImage.visibility = View.GONE

        if (user != null) {
            idTV.text = resources.getString(R.string.id, user.id)
            nameTV.text = user.name
            if (user.email == null) {
                emailTV.setText(R.string.no_email_permission)
                emailTV.setTextColor(Color.RED)
            } else {
                emailTV.text = user.email
                emailTV.setTextColor(Color.WHITE)
            }
            permissionsTV.text = user.permissions

            Picasso.get().load(user.picture).fit().placeholder(R.mipmap.ic_launcher_round).into(accountImage)

            idTV.visibility = View.VISIBLE
            nameTV.visibility = View.VISIBLE
            emailTV.visibility = View.VISIBLE
            permissionsTV.visibility = View.VISIBLE
            accountImage.visibility = View.VISIBLE
        }
    }
}