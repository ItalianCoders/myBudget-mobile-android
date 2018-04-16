package it.italiancoders.mybudget.manager

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.UiThread

/**
 * @author fattazzo
 *         <p/>
 *         date: 13/04/18
 */
@EBean
open class DialogManager {

    private var dialog: MaterialDialog? = null

    open fun openIndeterminateDialog(titleResId: Int, context: Context) {
        openIndeterminateDialog(context.resources.getString(titleResId), context)
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
}