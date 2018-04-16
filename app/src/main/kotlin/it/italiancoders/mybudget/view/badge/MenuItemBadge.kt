package it.italiancoders.mybudget.view.badge

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import it.italiancoders.mybudget.R

/**
 * @author donghua.xdh
 */
object MenuItemBadge {

    //public static void update(final MenuItem menu, int badgeCount) {
    //    update(null, menu, null, String.valueOf(formatNumber(badgeCount, true)));
    //}

    @JvmOverloads
    fun update(activity: Activity?, menu: MenuItem?, builder: MenuItemBadge.Builder?, listener: ActionItemBadgeListener? = null) {
        if (menu == null) return
        val badge: FrameLayout = menu.actionView as FrameLayout
        val badgeTextView: BadgeTextView?
        val imageView: ImageView?

        badgeTextView = badge.findViewById(R.id.menu_badge)
        imageView = badge.findViewById(R.id.menu_badge_icon)

        //Display icon in ImageView
        if (imageView != null && builder != null) {
            if (builder.iconDrawable != null) {
                imageView.setImageDrawable(builder.iconDrawable)
            }

            if (builder.iconTintColor != Color.TRANSPARENT) {
                imageView.setColorFilter(builder.iconTintColor)
            }
        }

        if (badgeTextView != null && builder != null && builder.textBackgroundColor != Color.TRANSPARENT) {
            badgeTextView.setBackgroundColor(builder.textBackgroundColor)
        }

        if (badgeTextView != null && builder != null && builder.textColor != Color.TRANSPARENT) {
            badgeTextView.setTextColor(builder.textColor)
        }


        //Bind onOptionsItemSelected to the activity
        if (activity != null) {
            badge.setOnClickListener {
                var consumed = false
                if (listener != null) {
                    consumed = listener.onOptionsItemSelected(menu)
                }
                if (!consumed) {
                    activity.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, menu)
                }
            }

            badge.setOnLongClickListener {
                val display = activity.windowManager.defaultDisplay
                val size = Point()
                display.getSize(size)
                val width = size.x
                val toast = Toast.makeText(activity, menu.title, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP, width / 5, convertDpToPx(activity, 48f))
                toast.show()
                true
            }
        }

        menu.isVisible = true
    }

    fun getBadgeTextView(menu: MenuItem?): BadgeTextView? {
        if (menu == null) {
            return null
        }
        val badge = menu.actionView as FrameLayout
        return badge.findViewById(R.id.menu_badge)
    }

    /**
     * hide the given menu item
     *
     * @param menu
     */
    fun hide(menu: MenuItem) {
        menu.isVisible = false
    }

    private fun convertDpToPx(context: Context, dp: Float): Int {
        return applyDimension(COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun formatNumber(value: Int, limitLength: Boolean): String {
        return when {
            value < 0 -> "-" + formatNumber(-value, limitLength)
            value < 100 -> java.lang.Long.toString(value.toLong())
            else -> "99+"
        }

    }

    interface ActionItemBadgeListener {
        fun onOptionsItemSelected(menu: MenuItem?): Boolean
    }

    class Builder {

        var textBackgroundColor: Int = 0 // TRANSPARENT = 0;
        var textColor: Int = 0 // TRANSPARENT = 0;
        var iconTintColor: Int = 0 // TRANSPARENT = 0;
        internal var iconDrawable: Drawable? = null

        fun textBackgroundColor(textBackgroundColor: Int): Builder {
            this.textBackgroundColor = textBackgroundColor
            return this
        }

        fun textColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        fun iconTintColor(iconTintColor: Int): Builder {
            this.iconTintColor = iconTintColor
            return this
        }

        fun iconDrawable(iconDrawable: Drawable): Builder {
            this.iconDrawable = iconDrawable
            return this
        }

    }
}