package it.italiancoders.mybudget.fragment.main.movements

import android.app.Activity
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.base.BindableView
import it.italiancoders.mybudget.fragment.movement.MovementFragment_
import it.italiancoders.mybudget.rest.RestClient
import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.utils.DataUtils
import it.italiancoders.mybudget.utils.FragmentUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/03/18
 */
@EViewGroup(R.layout.item_movement)
open class MovementView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr), BindableView<Movement> {

    @Bean
    internal lateinit var dataUtils: DataUtils

    @ViewById
    internal lateinit var dateTV: TextView

    @ViewById
    internal lateinit var amountTV: TextView

    @ViewById
    internal lateinit var noteTV: TextView
    @ViewById
    internal lateinit var noteImageView: ImageView

    @ViewById
    internal lateinit var usetTV: TextView

    @ViewById
    internal lateinit var categoryTV: TextView
    @ViewById
    internal lateinit var categoryImageView: ImageView

    @ViewById
    internal lateinit var detailsLayout: ConstraintLayout

    private lateinit var movement: Movement

    override fun bind(objectToBind: Movement) {
        this.movement = objectToBind

        usetTV.text = movement.executedBy?.alias ?: movement.executedBy?.username

        categoryTV.text = movement.category?.value.orEmpty()
        categoryImageView.setImageDrawable(dataUtils.getCategoryImage(movement.category))

        val timestamp = objectToBind.executedAt ?: 0
        val localTime = DataUtils.formatLocalDate(timestamp, pattern = "dd MMMM yyyy hh:mm")

        dateTV.text = localTime

        val sign = if (objectToBind.type == MovementType.Expense) "-" else "+"
        amountTV.text = dataUtils.formatCurrency(objectToBind.amount
                ?: 0.0, showSign = true, sign = sign)
        amountTV.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
        if (movement.category?.type != null) {
            when (movement.category?.type) {
                MovementType.Expense -> amountTV.setTextColor(ContextCompat.getColor(context, R.color.light_red))
                MovementType.Incoming -> amountTV.setTextColor(ContextCompat.getColor(context, R.color.primaryDarkColor))
                else -> {
                    amountTV.setTextColor(ContextCompat.getColor(context, R.color.primaryTextColor))
                }
            }
        }

        noteTV.text = objectToBind.note.orEmpty()

        noteTV.visibility = if (objectToBind.note.isNullOrBlank()) View.GONE else View.VISIBLE
        noteImageView.visibility = if (objectToBind.note.isNullOrBlank()) View.GONE else View.VISIBLE

        detailsLayout.visibility = View.GONE
    }

    @Click
    fun rootLayoutClicked() {
        detailsLayout.visibility = if (detailsLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    @Click
    fun editButtonClicked() {
        FragmentUtils.replace(context as Activity?, MovementFragment_.builder().movement(movement).build())
    }

    @Click
    fun deleteButtonClicked() {
        MaterialDialog.Builder(context)
                .iconRes(R.drawable.delete)
                .title(R.string.app_name)
                .content(R.string.remove_movement_dialog_text)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ ->
                    Config.currentAccountNeedReload = true
                    RestClient.movementService.delete(Config.currentAccount?.id.orEmpty(), movement.id!!).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                (context as Activity).recreate()
                            } else {
                                Toast.makeText(context, resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>?, t: Throwable?) {
                            Toast.makeText(context, resources.getString(R.string.error_try_later), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                .show()
    }
}