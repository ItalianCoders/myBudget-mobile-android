/*
 * Project: myBudget-mobile-android
 * File: InviteUserFragment.kt
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

package it.italiancoders.mybudget.fragment.account.invite

import android.widget.ListView
import android.widget.SearchView
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.user.UserInviteAdapter
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.AccountManager
import it.italiancoders.mybudget.rest.model.User
import org.androidannotations.annotations.*
import retrofit2.Call

/**
 * @author fattazzo
 *         <p/>
 *         date: 11/04/18
 */
@EFragment(R.layout.fragment_invite_user)
open class InviteUserFragment : BaseFragment(), SearchView.OnQueryTextListener {

    @ViewById
    internal lateinit var userSearchView: SearchView

    @ViewById
    internal lateinit var resultListView: ListView

    @JvmField
    @InstanceState
    internal var users: ArrayList<User> = arrayListOf()

    @Bean
    lateinit var accountManager: AccountManager

    private lateinit var userInviteAdapter: UserInviteAdapter

    private var currentCall : Call<List<User>>?= null

    @AfterViews
    fun initViews() {
        userSearchView.setOnQueryTextListener(this)

        userInviteAdapter = UserInviteAdapter(context!!, users)
        resultListView.adapter = userInviteAdapter
        resultListView.setItemChecked(if (users.isEmpty()) -1 else 0, true)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.orEmpty().length > 1) {
            currentCall?.cancel()
            currentCall = accountManager.searchUser(context!!, newText!!, object : Closure<List<User>> {
                override fun onSuccess(result: List<User>) {
                    users.clear()
                    users.addAll(result)
                    resultListView.adapter = UserInviteAdapter(context!!, users)
                    resultListView.setItemChecked(if (users.isEmpty()) -1 else 0, true)
                }
            })
        } else {
            users.clear()
            resultListView.adapter = UserInviteAdapter(context!!, users)
            resultListView.setItemChecked(-1,true)
        }
        return false
    }

    @Click
    fun inviteUserButtonClicked() {
        val pos = resultListView.checkedItemPosition
        val userSelected = resultListView.getItemAtPosition(pos) as User?

        userSelected?.let {
            MaterialDialog.Builder(activity!!)
                    .iconRes(R.drawable.help)
                    .title(R.string.intite_user_confirm_dialog_title)
                    .content(R.string.intite_user_confirm_dialog_message, it.alias, Config.currentAccount?.name)
                    .positiveText(android.R.string.yes)
                    .negativeText(android.R.string.no)
                    .onPositive { _, _ ->
                        run {
                            accountManager.inviteUser(context!!, userSelected.username)
                        }
                    }
                    .show()
        }
    }

    override fun backPressed(): Boolean {
        activity?.supportFragmentManager?.popBackStack()
        return true
    }

    override fun getActionBarTitleResId(): Int {
        return R.string.invite_user
    }
}