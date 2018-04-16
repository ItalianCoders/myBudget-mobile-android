package it.italiancoders.mybudget.view.user

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.utils.ImageUtils
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById


/**
 * @author fattazzo
 *         <p/>
 *         date: 11/04/18
 */
@EViewGroup(R.layout.item_user_invite)
open class UserInviteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<User> {

    @ViewById
    internal lateinit var aliasTV: TextView

    @ViewById
    internal lateinit var userImage: ImageView

    override fun bind(objectToBind: User) {
        aliasTV.text = objectToBind.alias.orEmpty()

        ImageUtils.loadImage(context, userImage, objectToBind.profileImageUrl, R.drawable.user, android.R.color.darker_gray)
    }
}