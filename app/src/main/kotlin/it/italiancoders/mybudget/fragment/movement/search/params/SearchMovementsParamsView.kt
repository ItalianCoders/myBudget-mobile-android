package it.italiancoders.mybudget.fragment.movement.search.params

import android.app.DatePickerDialog
import android.content.Context
import android.support.design.widget.TextInputEditText
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.Spinner
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.adapter.CategoryAdapter
import it.italiancoders.mybudget.adapter.user.UserAdapter
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.rest.model.User
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 06/04/18
 */
@EViewGroup(R.layout.view_search_movements_params)
open class SearchMovementsParamsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    @ViewById
    internal lateinit var yearTIET: TextInputEditText

    @ViewById
    internal lateinit var monthTIET: TextInputEditText

    @ViewById
    internal lateinit var dayTIET: TextInputEditText

    @ViewById
    internal lateinit var categoriaSpinner: Spinner

    @ViewById
    internal lateinit var userSpinner: Spinner

    private val categories: ArrayList<Category> by lazy {
        val catTmp = mutableListOf<Category>()
        catTmp.addAll(Config.currentAccount?.incomingCategoriesAvalaible.orEmpty())
        catTmp.addAll(Config.currentAccount?.expenseCategoriesAvalaible.orEmpty())

        val accountCategories = arrayListOf<Category>()
        accountCategories.addAll(catTmp.sortedWith(kotlin.Comparator { cat1, cat2 -> cat1.value.orEmpty().compareTo(cat2.value.orEmpty()) }))
        accountCategories
    }

    var searchMovementsParams: SearchMovementsParams? = null
        get() {
            val year = if (yearTIET.text.toString().isNotBlank()) yearTIET.text.toString().toInt() else null
            val month = if (monthTIET.text.toString().isNotBlank()) monthTIET.text.toString().toInt() else null
            val day = if (dayTIET.text.toString().isNotBlank()) dayTIET.text.toString().toInt() else null
            val category = if((categoriaSpinner.selectedItem as Category).id == null) null else categoriaSpinner.selectedItem as Category
            val user = if((userSpinner.selectedItem as User).username == resources.getString(R.string.all_m)) null else userSpinner.selectedItem as User

            yearTIET.updateError(year == null, R.string.required)
            monthTIET.updateError(month == null, R.string.required)

            val dataOk = year != null && month != null
            return if (dataOk) {
                SearchMovementsParams(year!!, month!!, day, user, category)
            } else {
                null
            }
        }
        set(value) {
            if (value != null) {
                yearTIET.text.clear()
                yearTIET.text.append(value.anno.toString())

                monthTIET.text.clear()
                monthTIET.text.append(value.mese.toString())

                dayTIET.text.clear()
                dayTIET.text.append(value.giorno?.toString() ?: "")

            }
            field = value
        }

    @AfterViews
    internal fun initViews() {
        val allCategory = Category()
        allCategory.value = context.getString(R.string.all_f)

        val categoryAdapter = CategoryAdapter(context, categories, allCategory)
        categoriaSpinner.adapter = categoryAdapter

        val allUser = User()
        allUser.username = context.getString(R.string.all_m)

        val userAdapter = UserAdapter(context, Config.currentAccount?.members.orEmpty(), allUser)
        userSpinner.adapter = userAdapter
    }

    @Click
    internal fun datePickerButtonClicked() {

        val newCalendar = Calendar.getInstance()

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            yearTIET.text.clear()
            yearTIET.text.append(year.toString())

            monthTIET.text.clear()
            monthTIET.text.append((month + 1).toString())

            dayTIET.text.clear()
            dayTIET.text.append(dayOfMonth.toString())
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun TextInputEditText.updateError(show: Boolean, errorResId: Int?, formatArg: Any? = null) {
        error = if (show) {
            resources.getString(errorResId!!, formatArg)
        } else {
            null
        }
    }
}