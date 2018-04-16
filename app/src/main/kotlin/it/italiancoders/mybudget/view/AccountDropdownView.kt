package it.italiancoders.mybudget.view

import android.content.Context
import android.util.AttributeSet
import it.italiancoders.mybudget.R
import org.androidannotations.annotations.EViewGroup

/**
 * @author fattazzo
 *         <p/>
 *         date: 07/04/18
 */
@EViewGroup(R.layout.item_dropdown_account)
open class AccountDropdownView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AccountView(context, attrs, defStyleAttr)