package it.italiancoders.mybudget.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById

/**
 * @author fattazzo
 *         <p/>
 *         date: 01/04/18
 */
@EViewGroup(R.layout.item_category)
open class CategoryView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), BindableView<Category> {

    @ViewById
    internal lateinit var valueTV: TextView

    @ViewById
    internal lateinit var iconImage: ImageView

    @Bean
    internal lateinit var dataUtils: DataUtils

    override fun bind(objectToBind: Category) {
        valueTV.text = objectToBind.value.orEmpty()

        iconImage.setImageDrawable(dataUtils.getCategoryImage(objectToBind))
        val color = when (objectToBind.type) {
            MovementType.Expense -> R.color.errorTextColor
            MovementType.Incoming -> R.color.primaryDarkColor
            else -> android.R.color.black
        }
        iconImage.imageTintList = ContextCompat.getColorStateList(context, color)
    }
}