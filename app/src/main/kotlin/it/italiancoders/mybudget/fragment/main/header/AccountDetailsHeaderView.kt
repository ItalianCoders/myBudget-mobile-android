package it.italiancoders.mybudget.fragment.main.header

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.TextView
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/03/18
 */
@EViewGroup(R.layout.view_dashboard_header)
open class AccountDetailsHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Bean
    internal lateinit var dataUtils: DataUtils

    @ViewById
    internal lateinit var incTotalContentTV: TextView

    @ViewById
    internal lateinit var expTotalContentTV: TextView

    @ViewById
    internal lateinit var diffTotalContentTV: TextView

    fun updateView() {

        val accountDetails = Config.currentAccount

        val incTot = accountDetails?.totalMonthlyIncoming ?: 0.0
        val expTot = accountDetails?.totalMonthlyExpense ?: 0.0

        val incTotStr = dataUtils.formatCurrency(incTot)
        val expTotStr = dataUtils.formatCurrency(expTot)
        val difTotStr = dataUtils.formatCurrency(incTot - expTot,showSign = true)

        incTotalContentTV.text = incTotStr
        expTotalContentTV.text = expTotStr
        diffTotalContentTV.text = difTotStr
    }
}