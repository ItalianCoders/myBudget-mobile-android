package it.italiancoders.mybudget.activity


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.fasterxml.jackson.core.type.TypeReference
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import it.italiancoders.mybudget.BaseTest
import it.italiancoders.mybudget.JsonTestHelper
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.action.ClickOnView
import it.italiancoders.mybudget.action.RecyclerViewItemCountAssertion
import it.italiancoders.mybudget.action.SlidingUpPanelViewAction
import it.italiancoders.mybudget.matcher.Matcher.withTextViewTextColor
import it.italiancoders.mybudget.matcher.RecyclerViewMatcher
import it.italiancoders.mybudget.rest.model.AccountDetails
import it.italiancoders.mybudget.rest.model.Movement
import it.italiancoders.mybudget.rest.model.MovementType
import it.italiancoders.mybudget.utils.DataUtils
import org.hamcrest.Matchers.not
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DashboardDataTest : BaseTest() {

    @Test
    fun headerDataTest() {

        performHeaderDataTest()

        rotateLandscape()
        performHeaderDataTest()

        rotatePortrait()
        performHeaderDataTest()
    }

    private fun performHeaderDataTest() {

        val accountDetails: AccountDetails = JsonTestHelper.getJsonObject(InstrumentationRegistry.getInstrumentation().context, "personalAccountDetails.json", object : TypeReference<AccountDetails>() {})
        val incTot = accountDetails.totalMonthlyIncoming ?: 0.0
        val expTot = accountDetails.totalMonthlyExpense ?: 0.0

        val incTotStr = dataUtils.formatCurrency(incTot)
        val expTotStr = dataUtils.formatCurrency(expTot)
        val difTotStr = dataUtils.formatCurrency(incTot - expTot,showSign = true)

        onView(withId(R.id.incTotalContentTV)).check(matches(withText(incTotStr)))
        onView(withId(R.id.incTotalContentTV)).check(matches(withTextViewTextColor(R.color.primaryColor)))

        onView(withId(R.id.expTotalContentTV)).check(matches(withText(expTotStr)))
        onView(withId(R.id.expTotalContentTV)).check(matches(withTextViewTextColor(R.color.light_red)))

        onView(withId(R.id.diffTotalContentTV)).check(matches(withText(difTotStr)))
        onView(withId(R.id.diffTotalContentTV)).check(matches(withTextViewTextColor(android.R.color.holo_blue_light)))
    }

    @Test
    fun chartDataTest() {
        val type = InstrumentationRegistry.getTargetContext().resources.getString(R.string.chart_type_expense)
        onView(withId(R.id.titleTV)).check(matches(withText("April 2018 - $type")))

        performChartTypeTest(R.string.chart_type_summary)
        rotateLandscape()
        performChartTypeTest(R.string.chart_type_summary)
        rotatePortrait()

        performChartTypeTest(R.string.chart_type_expense)
        rotateLandscape()
        performChartTypeTest(R.string.chart_type_expense)
        rotatePortrait()

        performChartTypeTest(R.string.chart_type_incoming)
        rotateLandscape()
        performChartTypeTest(R.string.chart_type_incoming)
        rotatePortrait()

        performChartTypeTest(R.string.chart_type_details)
        rotateLandscape()
        performChartTypeTest(R.string.chart_type_details)
        rotatePortrait()
    }

    private fun performChartTypeTest(typeResId: Int) {
        val title = "April 2018"
        val type = InstrumentationRegistry.getTargetContext().resources.getString(typeResId)

        onView(withId(R.id.chartTypeIV)).perform(click())
        onView(withText(type)).perform(click())
        onView(withId(R.id.titleTV)).check(matches(withText("$title - $type")))
    }

    @Test
    fun lastMovementsDataTest() {

        onView(withId(R.id.slidingLayout)).perform(SlidingUpPanelViewAction(SlidingUpPanelLayout.PanelState.EXPANDED))
        Thread.sleep(1000)

        for (i in 1..2) {
            onView(withId(R.id.movementsRecyclerView)).check(RecyclerViewItemCountAssertion(34))

            var index = 0
            val accountDetails: AccountDetails = JsonTestHelper.getJsonObject(InstrumentationRegistry.getInstrumentation().context, "personalAccountDetails.json", object : TypeReference<AccountDetails>() {})
            accountDetails.lastMovements.orEmpty().forEach {
                onView(withId(R.id.movementsRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

                var groupIndex = true
                while (groupIndex && index <= 34) {
                    groupIndex = try {
                        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.dayTV)).check(matches(isDisplayed()))
                        // date grop
                        performMovementGroupTest(index, it)
                        index++
                        true
                    } catch (e: NoMatchingViewException) {
                        false
                    }
                }
                // movement
                performMovementViewTest(index, it)

                index++
            }

            rotateLandscape()
        }

        rotatePortrait()
    }

    private fun performMovementGroupTest(index: Int, movement: Movement) {
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.dayTV))
                .check(matches(withText(DataUtils.formatLocalDate(movement.executedAt
                        ?: 0, pattern = "dd"))))
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.monthTV))
                .check(matches(withText(DataUtils.formatLocalDate(movement.executedAt
                        ?: 0, pattern = "MMMM"))))
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.yearTV))
                .check(matches(withText(DataUtils.formatLocalDate(movement.executedAt
                        ?: 0, pattern = "yyyy"))))
    }

    private fun performMovementViewTest(index: Int, movement: Movement) {
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.usetTV))
                .check(matches(withText(movement.executedBy?.alias
                        ?: movement.executedBy?.username)))

        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.categoryTV))
                .check(matches(withText(movement.category?.value.orEmpty())))

        val timestamp = movement.executedAt ?: 0
        val localTime = DataUtils.formatLocalDate(timestamp, pattern = "dd MMMM yyyy hh:mm")
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.dateTV))
                .check(matches(withText(localTime)))

        val sign = if (movement.type == MovementType.Expense) "-" else "+"
        val amount = dataUtils.formatCurrency(movement.amount
                ?: 0.0, showSign = true, sign = sign)
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.amountTV))
                .check(matches(withText(amount)))

        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.noteTV)).check(matches(withText(movement.note.orEmpty())))
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.detailsLayout)).check(matches(not(isDisplayed())))

        onView(withId(R.id.movementsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ClickOnView(R.id.rootLayout)))
        onView(withId(R.id.movementsRecyclerView)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.detailsLayout)).check(matches(isDisplayed()))
        if (movement.note.isNullOrBlank()) {
            viewNotDisplayed(onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.noteTV)))
        } else {
            onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.noteTV)).check(matches(isDisplayed()))
        }

        onView(withId(R.id.movementsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ClickOnView(R.id.rootLayout)))
        onView(RecyclerViewMatcher(R.id.movementsRecyclerView).atPositionOnView(index, R.id.detailsLayout)).check(matches(not(isDisplayed())))
    }

    private fun viewNotDisplayed(view: ViewInteraction) {
        try {
            view.check(matches(not(isDisplayed())))
        } catch (e: Exception) {
            view.check(doesNotExist())
        }

    }
}
