package it.italiancoders.mybudget.fragment.main.chart

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import it.italiancoders.mybudget.Config
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.preferences.ApplicationPreferenceManager
import it.italiancoders.mybudget.rest.model.Category
import it.italiancoders.mybudget.utils.DataUtils
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EViewGroup
import org.androidannotations.annotations.ViewById
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author fattazzo
 *         <p/>
 *         date: 30/03/18
 */
@EViewGroup(R.layout.view_dashboard_chart)
open class AccountDetailsChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Bean
    internal lateinit var dataUtils: DataUtils

    @ViewById
    internal lateinit var dashboardChart: PieChart

    @ViewById
    internal lateinit var titleTV: TextView

    @Bean
    internal lateinit var preferenceManager: ApplicationPreferenceManager

    fun updateView() {
        setupTitle()
        setupChart()
    }

    private fun setupTitle() {
        var titleString = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Calendar.getInstance().time).capitalize()
        titleString += " - " + resources.getStringArray(R.array.dashboard_chart_type_name)[preferenceManager.getChartType()]

        titleTV.text = titleString
    }

    @Click
    internal fun chartTypeIVClicked(view: View) {
        val popup = PopupMenu(context, view)
        popup.menuInflater.inflate(R.menu.popup_chart_type, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            var chartType = 1
            when (item.itemId) {
                R.id.item_summary -> chartType = 0
                R.id.item_expense -> chartType = 1
                R.id.item_incoming -> chartType = 2
                R.id.item_details -> chartType = 3
            }
            preferenceManager.prefs.edit().lastChartType().put(chartType).apply()
            updateView()
            true
        }
        popup.show()
    }

    private fun setupChart() {
        dashboardChart.setUsePercentValues(true)
        dashboardChart.description.isEnabled = false
        dashboardChart.setExtraOffsets(15f, 15f, 15f, 15f)

        dashboardChart.dragDecelerationFrictionCoef = 0.95f

        //dashboardChart.centerText = generateCenterSpannableText(accountDetails)

        dashboardChart.setDrawEntryLabels(preferenceManager.getChartType() >= 0)

        dashboardChart.isDrawHoleEnabled = true
        dashboardChart.setHoleColor(Color.WHITE)

        dashboardChart.setTransparentCircleColor(Color.WHITE)
        dashboardChart.setTransparentCircleAlpha(110)

        dashboardChart.holeRadius = 58f
        dashboardChart.transparentCircleRadius = 61f

        dashboardChart.setDrawCenterText(true)

        dashboardChart.rotationAngle = 45f
        dashboardChart.isRotationEnabled = true
        dashboardChart.isHighlightPerTapEnabled = true

        setData()

        dashboardChart.animateY(1400, Easing.EasingOption.EaseInOutQuad)
        // mChart.spin(2000, 0, 360);

        dashboardChart.legend.isEnabled = false

        // entry label styling
        dashboardChart.setEntryLabelColor(Color.BLACK)
        dashboardChart.setEntryLabelTextSize(12f)
    }

    private fun setData() {

        val entries = buildDataForChartType()

        val dataSet = PieDataSet(entries, "")

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(-0f, 0f)
        dataSet.selectionShift = 5f
        dataSet.setDrawIcons(true)
        //dataSet.isDrawValuesEnabled = false

        dataSet.valueLinePart1OffsetPercentage = 80f
        dataSet.valueLinePart1Length = 0.2f
        dataSet.valueLinePart2Length = 0.4f
        dataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        dataSet.colors = preferenceManager.chartColorTheme.colors.asList()
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        //data.setValueFormatter(DefaultValueFormatter(2))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
        dashboardChart.data = data

        // undo all highlights
        dashboardChart.highlightValues(null)

        dashboardChart.invalidate()
    }

    private fun buildDataForChartType(): List<PieEntry> {
        var entries = ArrayList<PieEntry>()

        val accountDetails = Config.currentAccount
        if (accountDetails != null) {
            val categoryMap = mutableMapOf<String, Category>()
            accountDetails.expenseCategoriesAvailable.orEmpty().forEach { categoryMap[it.id.orEmpty()] = it }
            accountDetails.incomingCategoriesAvailable.orEmpty().forEach { categoryMap[it.id.orEmpty()] = it }

            var expTotal = 0F
            val expensDataMap = mutableMapOf<Category, Float>()
            accountDetails.expenseOverviewMovement.orEmpty().forEach {
                expensDataMap[categoryMap[it.key]!!] = it.value.toFloat()
                expTotal += it.value.toFloat()
            }

            var incTotal = 0F
            val incomingDataMap = mutableMapOf<Category, Float>()
            accountDetails.incomingOverviewMovement.orEmpty().forEach {
                incomingDataMap[categoryMap[it.key]!!] = it.value.toFloat()
                incTotal += it.value.toFloat()
            }

            when (preferenceManager.getChartType()) {
                0 -> {
                    entries.add(PieEntry(incTotal, resources.getString(R.string.incoming)))
                    entries.add(PieEntry(expTotal, resources.getString(R.string.expense)))
                }
                1 -> {
                    entries = mergeLowerData(expensDataMap, expTotal)
                }
                2 -> {
                    entries = mergeLowerData(incomingDataMap, incTotal)
                }
                3 -> {
                    expensDataMap.putAll(incomingDataMap)
                    entries = mergeLowerData(expensDataMap, expTotal + incTotal)
                }
            }
        }
        return entries
    }

    private fun mergeLowerData(dataMap: MutableMap<Category, Float>, total: Float): ArrayList<PieEntry> {
        val entries = ArrayList<PieEntry>()

        // sort map by amount
        val result = dataMap.toList().sortedBy { (_, value) -> value }.toMap()

        val percentageMap = mutableMapOf<Category, Float>()
        result.forEach { entry -> percentageMap[entry.key] = entry.value / total * 100 }

        var lowValuesCategory: Category? = null
        var lowValuesAmount = 0F
        var lowValuesPercentage = 0F
        percentageMap.forEach {
            if (lowValuesPercentage < 10F && percentageMap.size > 2) {
                lowValuesAmount += dataMap[it.key] ?: 0F
                lowValuesPercentage += it.value
                if (lowValuesCategory == null) {
                    lowValuesCategory = Category()
                    lowValuesCategory!!.value = it.key.value.orEmpty()
                } else {
                    lowValuesCategory!!.value += ",${it.key.value.orEmpty()}"
                }
            } else {
                if (lowValuesCategory != null) {
                    entries.add(PieEntry(lowValuesAmount, resources.getString(R.string.others),dataUtils.getCategoryImage(lowValuesCategory!!)))
                    lowValuesCategory = null
                }
                entries.add(PieEntry(dataMap[it.key] ?: 0F, it.key.value.orEmpty(),dataUtils.getCategoryImage(it.key)))
            }
        }
        if (lowValuesCategory != null) {
            entries.add(PieEntry(lowValuesAmount, lowValuesCategory!!.value.orEmpty(),dataUtils.getCategoryImage(lowValuesCategory!!)))
        }

        return entries
    }
}