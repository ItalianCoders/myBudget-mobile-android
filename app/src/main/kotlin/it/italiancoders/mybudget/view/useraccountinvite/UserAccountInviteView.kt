package it.italiancoders.mybudget.view.useraccountinvite

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.PendingInvitesManager
import it.italiancoders.mybudget.rest.model.InviteStatus
import it.italiancoders.mybudget.rest.model.UserAccountInvite
import it.italiancoders.mybudget.utils.ImageUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EViewGroup(R.layout.item_user_account_invite)
open class UserAccountInviteView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<UserAccountInvite> {

    @ViewById
    internal lateinit var aliasTV: TextView

    @ViewById
    internal lateinit var userImage: ImageView

    @Bean
    lateinit var pendingInvitesManager: PendingInvitesManager

    private lateinit var userAccountInvite: UserAccountInvite

    override fun bind(objectToBind: UserAccountInvite) {
        userAccountInvite = objectToBind

        aliasTV.text = objectToBind.invitedBy.alias.orEmpty()
        ImageUtils.loadImage(context, userImage, objectToBind.invitedBy.profileImageUrl, R.drawable.user, android.R.color.darker_gray)
    }

    @Click
    fun confirmButtonClicked() {
        sendReply(InviteStatus.Confirmed)
    }

    @Click
    fun rejectButtonClicked() {
        sendReply(InviteStatus.Declined)
    }

    private fun sendReply(inviteStatus: InviteStatus) {
        pendingInvitesManager.reply(context, userAccountInvite.id, inviteStatus, object : Closure<Void> {
            override fun onSuccess() {
                Config.currentAccountNeedReload = true
                (context as Activity).recreate()
            }
        })
    }
}