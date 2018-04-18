package it.italiancoders.mybudget.fragment.main

import android.graphics.Color
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.fasterxml.jackson.databind.ObjectMapper
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.MainActivity
import it.italiancoders.mybudget.adapter.account.AccountAdapter
import it.italiancoders.mybudget.fragment.BaseFragment
import it.italiancoders.mybudget.fragment.account.AccountFragment_
import it.italiancoders.mybudget.fragment.account.invite.InviteUserFragment_
import it.italiancoders.mybudget.fragment.main.chart.AccountDetailsChartView
import it.italiancoders.mybudget.fragment.main.header.AccountDetailsHeaderView
import it.italiancoders.mybudget.fragment.main.movements.AccountDetailsLastMovementsView
import it.italiancoders.mybudget.fragment.movement.MovementFragment_
import it.italiancoders.mybudget.fragment.notification.NotificationsFragment
import it.italiancoders.mybudget.fragment.notification.NotificationsFragment_
import it.italiancoders.mybudget.manager.Closure
import it.italiancoders.mybudget.manager.rest.AccountManager
import it.italiancoders.mybudget.preferences.ApplicationPreferenceManager
import it.italiancoders.mybudget.rest.model.Account
import it.italiancoders.mybudget.rest.model.AccountDetails
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.utils.FragmentUtils
import it.italiancoders.mybudget.view.WaitingView
import it.italiancoders.mybudget.view.badge.MenuItemBadge
import kotlinx.android.synthetic.main.app_bar_main.*
import org.androidannotations.annotations.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 25/03/18
 */
@EFragment(R.layout.fragment_main)
open class MainFragment : BaseFragment(), SlidingUpPanelLayout.PanelSlideListener, View.OnClickListener {

    @ViewById
    internal lateinit var accountDetailsHeaderView: AccountDetailsHeaderView

    @ViewById
    internal lateinit var accountDetailsChartView: AccountDetailsChartView

    @ViewById
    internal lateinit var accountDetailsLastMovementsView: AccountDetailsLastMovementsView

    @ViewById
    internal lateinit var incomingFAB: FloatingActionButton

    @ViewById
    internal lateinit var expenseFAB: FloatingActionButton

    @ViewById
    internal lateinit var loadingLayout: WaitingView

    @JvmField
    @ViewById
    internal var slidingLayout: SlidingUpPanelLayout? = null

    @Bean
    internal lateinit var preferenceManager: ApplicationPreferenceManager

    @Bean
    internal lateinit var accountManager: AccountManager

    @AfterViews
    internal fun initView() {
        hideProgressBar()

        updateUI()

        if (slidingLayout != null) {
            slidingLayout!!.removePanelSlideListener(this)
            slidingLayout!!.addPanelSlideListener(this)
        }

        (activity as MainActivity).accountTV.setOnClickListener(this)
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun showProgressBar() {
        loadingLayout.visibility = View.VISIBLE
        loadingLayout.setText(R.string.loading_dashboard)
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun hideProgressBar() {
        loadingLayout.visibility = View.GONE
        loadingLayout.text = ""
    }

    open fun loadAccountDetails(accountId: String) {
        showProgressBar()
        accountManager.loadAccountDetails(context!!, accountId, false, object : Closure<AccountDetails?> {
            override fun onSuccess(result: AccountDetails?) {
                hideProgressBar()
                Config.currentAccount = result
                Config.currentAccountNeedReload = false
                updateUI()
            }

            override fun onError() {
                hideProgressBar()
            }

            override fun onFailure() {
                hideProgressBar()
            }
        })
    }

    @UiThread
    open fun updateUI() {
        if (Config.currentAccountNeedReload) {
            loadAccountDetails(Config.currentAccount?.id
                    ?: Config.accounts.orEmpty().firstOrNull()?.id.orEmpty())
        } else {
            accountDetailsHeaderView.updateView()
            accountDetailsChartView.updateView()
            accountDetailsLastMovementsView.updateView()

            updateFAB()

            (activity as MainActivity).updateAccountTitle()

            //MenuItemBadge.getBadgeTextView((activity as AppCompatActivity).toolbar?.menu?.findItem(R.id.menuMessage))?.setBadgeCount(Config.currentAccount?.numberOfPendingAccountInvites ?: 0)
            //MenuItemBadge.getBadgeTextView((activity as AppCompatActivity).toolbar?.menu?.findItem(R.id.menuNotification))?.setBadgeCount(0)
            updateNofitications()
        }
    }

    @UiThread
    open fun updateFAB() {
        if (slidingLayout != null) {
            if (slidingLayout!!.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                incomingFAB.hide()
                expenseFAB.hide()
            }
        }
    }

    @Click
    fun expenseFABClicked() {
        openNewMovement(MovementType.Expense)
    }

    @Click
    fun incomingFABClicked() {
        openNewMovement(MovementType.Incoming)
    }

    private fun openNewMovement(movementType: MovementType) {
        if (loadingLayout.visibility == View.GONE)
            FragmentUtils.replace(activity, MovementFragment_.builder().movementType(movementType).build())
    }

    override fun backPressed(): Boolean {
        if (slidingLayout != null && slidingLayout!!.panelState != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            slidingLayout!!.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        } else {
            MaterialDialog.Builder(activity!!)
                    .iconRes(R.mipmap.ic_launcher_round)
                    .title(R.string.app_name)
                    .content(R.string.app_exit_dialog_content)
                    .positiveText(android.R.string.yes)
                    .negativeText(android.R.string.no)
                    .onPositive { _, _ -> activity?.finish() }
                    .show()
        }

        return false
    }

    override fun isAccountsSelectionVisible(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        accountManager.loadAll(context!!, object : Closure<List<Account>> {
            override fun onSuccess(result: List<Account>) {
                Config.accounts = result
                accountLoaded()
            }
        })
    }

    @UiThread
    open fun accountLoaded() {
        preferenceManager.prefs.edit().lastUserAccounts().put(ObjectMapper().writeValueAsString(Config.accounts)).apply()

        val accountAdapter = AccountAdapter(context!!)
        val dialog = MaterialDialog.Builder(activity!!)
                .iconRes(R.mipmap.ic_launcher_round)
                .title(R.string.app_name)
                .content(R.string.account_select)
                .adapter(accountAdapter, null)
                .alwaysCallSingleChoiceCallback().build()
        accountAdapter.accountCallback = object : AccountAdapter.AccountCallback {
            override fun onAccountSelected(account: Account) {
                dialog?.dismiss()
                loadAccountDetails(account.id)
            }
        }
        dialog.show()
    }

    override fun getActionBarTitle(): String? = ""

    // ----------- Slide panel listener ----------------
    override fun onPanelSlide(panel: View?, slideOffset: Float) {
    }

    override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
        when (newState) {
            SlidingUpPanelLayout.PanelState.DRAGGING, SlidingUpPanelLayout.PanelState.EXPANDED -> {
                incomingFAB.hide()
                expenseFAB.hide()
            }
            else -> {
                incomingFAB.show()
                expenseFAB.show()
            }
        }
    }

    override fun createFragmentOptionMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main, menu)

        val menuItemNotification = menu!!.findItem(R.id.menuNotification)
        MenuItemBadge.update(activity, menuItemNotification, MenuItemBadge.Builder()
                .iconDrawable(ContextCompat.getDrawable(context!!, R.drawable.notifications)!!)
                .iconTintColor(Color.WHITE)
                .textBackgroundColor(ContextCompat.getColor(context!!, android.R.color.holo_blue_dark))
                .textColor(Color.WHITE))
        //MenuItemBadge.getBadgeTextView(menuItemNotification)?.setBadgeCount(0)


        val menuItemMessage = menu.findItem(R.id.menuNotificationUserInvite)
        MenuItemBadge.update(activity, menuItemMessage, MenuItemBadge.Builder()
                .iconDrawable(ContextCompat.getDrawable(context!!, R.drawable.user)!!)
                .iconTintColor(Color.WHITE)
                .textBackgroundColor(Color.parseColor("#EF4738"))
                .textColor(Color.WHITE))
        //MenuItemBadge.getBadgeTextView(menuItemMessage)?.setBadgeCount(Config.currentAccount?.numberOfPendingAccountInvites ?: 0)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        updateNofitications()
    }

    private fun updateNofitications() {
        val userInviteNotifications = Config.currentAccount?.numberOfPendingAccountInvites ?: 0
        MenuItemBadge.getBadgeTextView((activity as AppCompatActivity).toolbar?.menu?.findItem(R.id.menuNotificationUserInvite))?.setBadgeCount(userInviteNotifications)
        (activity as AppCompatActivity).toolbar?.menu?.findItem(R.id.menuNotificationUserInvite)?.isVisible = userInviteNotifications > 0

        MenuItemBadge.getBadgeTextView((activity as AppCompatActivity).toolbar?.menu?.findItem(R.id.menuNotification))?.setBadgeCount(0)
    }

    // ------------------- Menu --------------------.....................................

    @OptionsItem
    fun menuRefreshSelected() {
        Config.currentAccountNeedReload = true
        initView()
    }

    @OptionsItem
    fun menuInviteUserSelected() {
        FragmentUtils.add(activity, InviteUserFragment_.builder().build(), animationType = FragmentUtils.AnimationType.FADE_IN)
    }

    @OptionsItem
    fun menuNotificationUserInviteSelected() {
        FragmentUtils.replace(activity, NotificationsFragment_.builder().notificationType(NotificationsFragment.NotificationType.USER_INVITE).build(), animationType = FragmentUtils.AnimationType.FADE_IN)
    }

    @OptionsItem
    fun menuNotificationSelected() {
        //MenuItemBadge.getBadgeTextView(menu.findItem(R.id.menuNotification))?.visibility = View.VISIBLE
        //MenuItemBadge.getBadgeTextView(menu.findItem(R.id.menuNotification))?.visibility = View.GONE
        FragmentUtils.replace(activity, NotificationsFragment_.builder().notificationType(NotificationsFragment.NotificationType.GENERAL).build(), animationType = FragmentUtils.AnimationType.FADE_IN)
    }

    @OptionsItem
    fun menuEditAccountSelected() {
        FragmentUtils.replace(activity, AccountFragment_.builder().account(Config.currentAccount).build(), animationType = FragmentUtils.AnimationType.SLIDE)
    }

    @OptionsItem
    fun menuNewAccountSelected() {
        FragmentUtils.replace(activity, AccountFragment_.builder().build(), animationType = FragmentUtils.AnimationType.SLIDE)
    }
}