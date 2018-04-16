package it.italiancoders.mybudget.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import it.italiancoders.mybudget.R
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 05/04/18
 */
@EViewGroup(R.layout.view_waiting)
open class WaitingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @ViewById
    internal lateinit var textTV: TextView

    internal var text: String? = null

    @AfterViews
    internal fun init() {
        textTV.text = text.orEmpty()
    }

    fun hide() {
        this.visibility = View.GONE
    }

    fun show() {
        this.visibility = View.VISIBLE
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setText(textResId: Int) {
        this.text = resources.getString(textResId)
    }
}