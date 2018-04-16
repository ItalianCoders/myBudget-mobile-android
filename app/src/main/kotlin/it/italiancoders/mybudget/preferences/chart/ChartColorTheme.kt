package it.italiancoders.mybudget.preferences.chart

import android.graphics.Color

import com.github.mikephil.charting.utils.ColorTemplate.rgb

/**
 * @author fattazzo
 *
 *
 * date: 01/09/17
 */
enum class ChartColorTheme constructor(val colors: IntArray) {

    LIBERTY(intArrayOf(Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130))),
    JOYFUL(intArrayOf(Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209))),
    PASTEL(intArrayOf(Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80))),
    COLORFUL(intArrayOf(Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53))),
    VORDIPLOM(intArrayOf(Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157))),
    MATERIAL(intArrayOf(rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db"), rgb("#ff9800")))
}
