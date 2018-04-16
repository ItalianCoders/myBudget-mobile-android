package it.italiancoders.mybudget.preferences.chart

import android.app.AlertDialog
import android.content.Context
import android.preference.ListPreference
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import it.italiancoders.mybudget.R
import java.util.*

class ChartColorThemePickerPreference(context: Context?, attrs: AttributeSet?) : ListPreference(context, attrs) {
    private var currentIndex = 0
    private var image1: ImageView? = null
    private var image2: ImageView? = null
    private var image3: ImageView? = null
    private var image4: ImageView? = null
    private var image5: ImageView? = null
    private var chartColorThemes: Array<ChartColorTheme>? = null
    private var icons: MutableList<IconItem>? = null

    private var selectedTheme: ChartColorTheme? = null
    private var summary: TextView? = null


    override fun onBindView(view: View) {
        super.onBindView(view)

        view.setPadding(50, 50, 50, 50)

        val themes = ChartColorTheme.values()
        selectedTheme = themes[currentIndex]
        image1 = view.findViewById(R.id.color1)
        image2 = view.findViewById(R.id.color2)
        image3 = view.findViewById(R.id.color3)
        image4 = view.findViewById(R.id.color4)
        image5 = view.findViewById(R.id.color5)
        updateIcons()
        summary = view.findViewById(R.id.summary)
        summary!!.text = themes[currentIndex].name
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)
        if (icons != null) {
            for (i in chartColorThemes!!.indices) {
                val item = icons!![i]
                if (item.isChecked) {
                    persistString("" + i)
                    currentIndex = i
                    selectedTheme = item.theme
                    updateIcons()
                    summary!!.text = item.theme.name
                    break
                }
            }
        }
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        var number = "3"
        if (restorePersistedValue) {
            number = this.getPersistedString("3")
        } else {
            persistString(number)
        }
        currentIndex = try {
            Integer.parseInt(number)
        } catch (e: Exception) {
            3
        }

    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton(null, null)

        icons = ArrayList()
        chartColorThemes = ChartColorTheme.values()
        for (i in chartColorThemes!!.indices) {
            val item = IconItem(chartColorThemes!![i], i == currentIndex)
            icons!!.add(item)
        }
        val customListPreferenceAdapter = ChartColorThemePickerPreferenceAdapter(
                context, R.layout.preference_list_chart_theme_picker, icons as ArrayList<IconItem>)
        builder.setAdapter(customListPreferenceAdapter, null)
    }

    private fun updateIcons() {
        image1!!.setBackgroundColor(selectedTheme!!.colors[0])
        image1!!.tag = selectedTheme
        image2!!.setBackgroundColor(selectedTheme!!.colors[1])
        image2!!.tag = selectedTheme
        image3!!.setBackgroundColor(selectedTheme!!.colors[2])
        image3!!.tag = selectedTheme
        image4!!.setBackgroundColor(selectedTheme!!.colors[3])
        image4!!.tag = selectedTheme
        image5!!.setBackgroundColor(selectedTheme!!.colors[4])
        image5!!.tag = selectedTheme
    }

    private inner class ChartColorThemePickerPreferenceAdapter internal constructor(context: Context, private val resource: Int, private val icons: List<IconItem>) : ArrayAdapter<IconItem>(context, resource, icons) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView

            val holder: ViewHolder
            if (view == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(resource, parent, false)

                holder = ViewHolder()
                holder.iconName = view!!.findViewById(R.id.iconName)
                holder.image1 = view.findViewById(R.id.color1)
                holder.image2 = view.findViewById(R.id.color2)
                holder.image3 = view.findViewById(R.id.color3)
                holder.image4 = view.findViewById(R.id.color4)
                holder.image5 = view.findViewById(R.id.color5)
                holder.radioButton = view.findViewById(R.id.iconRadio)
                view.tag = holder
            } else {
                holder = view.tag as ViewHolder
            }

            holder.iconName!!.text = icons[position].theme.name

            val theme = icons[position].theme
            holder.image1!!.setBackgroundColor(theme.colors[0])
            holder.image2!!.setBackgroundColor(theme.colors[1])
            holder.image3!!.setBackgroundColor(theme.colors[2])
            holder.image4!!.setBackgroundColor(theme.colors[3])
            holder.image5!!.setBackgroundColor(theme.colors[4])
            holder.radioButton!!.isChecked = icons[position].isChecked
            view.setOnClickListener { _ ->
                for (i in icons.indices) {
                    icons[i].isChecked = i == position
                }
                dialog.dismiss()
            }
            return view
        }
    }

    private inner class IconItem internal constructor(val theme: ChartColorTheme, var isChecked: Boolean)

    private inner class ViewHolder {
        internal var image1: ImageView? = null
        internal var image2: ImageView? = null
        internal var image3: ImageView? = null
        internal var image4: ImageView? = null
        internal var image5: ImageView? = null
        internal var iconName: TextView? = null
        internal var radioButton: RadioButton? = null
    }
}