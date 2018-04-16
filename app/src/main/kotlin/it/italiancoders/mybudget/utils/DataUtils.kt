package it.italiancoders.mybudget.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.preferences.ApplicationPreferenceManager
import it.italiancoders.mybudget.rest.model.Category
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import org.androidannotations.annotations.RootContext
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author fattazzo
 *         <p/>
 *         date: 31/03/18
 */
@EBean(scope = EBean.Scope.Singleton)
open class DataUtils {

    @Bean
    internal lateinit var preferenceManager: ApplicationPreferenceManager

    @RootContext
    internal lateinit var context: Context

    /**
     * Format number to currency whit symbol configured in app preferences.
     *
     * @return formatted number
     */
    fun formatCurrency(value: Double, showSign: Boolean = false, sign: String? = null): String {
        val signPos = sign ?: "+"
        val signNeg = sign ?: "-"

        var pattern = "#,##0.00${preferenceManager.getCurrency().symbol}"
        if (showSign) {
            pattern = "$signPos#,##0.00${preferenceManager.getCurrency().symbol};$signNeg#,##0.00${preferenceManager.getCurrency().symbol}"
        }
        val decimalFormat = DecimalFormat(pattern)

        return decimalFormat.format(value)
    }

    fun getCategoryImage(category: Category?): Drawable {
        val defaultImage = ContextCompat.getDrawable(context, R.drawable.category)!!
        return try {
            val icons = context.resources.obtainTypedArray(R.array.cateogory_icons)
            val img = if (category?.iconId != null) icons.getDrawable(category.iconId!!) else defaultImage
            icons.recycle()
            img
        } catch (e: Exception) {
            defaultImage
        }
    }


    companion object {

        /**
         * Format UTC milliseconds to local date.
         *
         * @param utcMillisec milliseconds
         * @param pattern format pattern
         *
         * @return formatted date
         */
        fun formatLocalDate(utcMillisec: Long, pattern: String = "dd MMMM yyyy"): String {
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.timeZone = Calendar.getInstance().timeZone

            return try {
                sdf.format(Date(utcMillisec * 1000))
            } catch (e: Exception) {
                ""
            }
        }

        fun toUnixDate(date: Date?): Long? {
            return if (date == null) {
                null
            } else {
                date.time / 1000
            }
        }
    }
}