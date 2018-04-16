package it.italiancoders.mybudget.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.MainActivity
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.UiThread


/**
 * @author fattazzo
 *         <p/>
 *         date: 25/01/18
 */
@EFragment
open class BaseFragment : Fragment() {

    private var dialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun backPressed(): Boolean {
        return false
    }

    open fun openIndeterminateDialog(title: String) {
        openIndeterminateDialog(title, activity!!)
    }

    open fun openIndeterminateDialog(titleResId: Int) {
        openIndeterminateDialog(resources.getString(titleResId), activity!!)
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun openIndeterminateDialog(title: String, context: Context) {
        if (dialog == null || !dialog!!.isShowing) {
            dialog = MaterialDialog.Builder(context)
                    .title(title)
                    .content(R.string.dialog_wait_content)
                    .progress(true, 0)
                    .cancelable(false)
                    .build()
            dialog!!.show()
        }
    }

    @UiThread(propagation = UiThread.Propagation.ENQUEUE)
    open fun closeIndeterminateDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }

    private fun setActionBarTitle() {
        if (getActionBarTitle() == null) {
            (activity as AppCompatActivity).supportActionBar?.setTitle(getActionBarTitleResId())
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = getActionBarTitle()
        }
    }

    open fun getActionBarTitleResId(): Int {
        return R.string.app_name
    }

    open fun getActionBarTitle(): String? {
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        for (i in 0 until (menu?.size() ?: 0)) {
            val drawable = menu?.getItem(i)?.icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(ContextCompat.getColor(activity!!, R.color.primaryTextColorInverted), PorterDuff.Mode.SRC_ATOP)
            }
        }
        updateAccountsSelectionView()
        setActionBarTitle()
    }

    private fun updateAccountsSelectionView() {
        (activity!! as MainActivity).toggleAccountTitle(isAccountsSelectionVisible())
    }

    open fun isAccountsSelectionVisible(): Boolean {
        return false
    }
}