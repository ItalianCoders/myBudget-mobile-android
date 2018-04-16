package it.italiancoders.mybudget.adapter.useraccountinvitegroup

import it.italiancoders.mybudget.rest.model.User
import it.italiancoders.mybudget.rest.model.UserAccountInvite

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
class UserAccountInviteGroup(createdAt: Long) : UserAccountInvite() {

    init {
        id = ""

        user = User()

        invitedBy = User()

        this.createdAt = createdAt
    }
}