package com.gitlab.nastyaka.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.uiautomator.UiDevice

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class ExampleInstrumentedTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    private var device: UiDevice =
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.gitlab.nastyaka.calculator", appContext.packageName)
    }

    @Test
    fun button0Test() {
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("0")))
    }

    @Test
    fun button0ManyClickTest() {
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("0")))
    }

    @Test
    fun button1Test() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("1")))
    }

    @Test
    fun button2Test() {
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("2")))
    }

    @Test
    fun button3Test() {
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("3")))
    }

    @Test
    fun button4Test() {
        onView(withId(R.id.button4)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("4")))
    }

    @Test
    fun button5Test() {
        onView(withId(R.id.button5)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("5")))
    }

    @Test
    fun button6Test() {
        onView(withId(R.id.button6)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("6")))
    }

    @Test
    fun button7Test() {
        onView(withId(R.id.button7)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("7")))
    }

    @Test
    fun button8Test() {
        onView(withId(R.id.button8)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("8")))
    }

    @Test
    fun button9Test() {
        onView(withId(R.id.button9)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("9")))
    }

    @Test
    fun buttonClrTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button4)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("1234")))
        onView(withId(R.id.buttonClr)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("")))
    }

    @Test
    fun negativeConstTest() {
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button9)).perform(click())
        onView(withId(R.id.button8)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("-98")))
    }

    @Test
    fun buttonAddTest() {
        onView(withId(R.id.button9)).perform(click())
        onView(withId(R.id.buttonPls)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("12")))
    }

    @Test
    fun buttonAddZeroesTest() {
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.buttonPls)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("0+0")))
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("0")))
    }

    @Test
    fun buttonSubTest() {
        onView(withId(R.id.button9)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("89")))
    }

    @Test
    fun buttonSubSubTest() {
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button9)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("-95")))
    }

    @Test
    fun buttonSubAddTest() {
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button9)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonPls)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("-89")))
    }

    @Test
    fun buttonMulTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonMul)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("36")))
    }

    @Test
    fun buttonSubMulTest() {
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonMul)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("-36")))
    }

    @Test
    fun buttonDivTest() {
        onView(withId(R.id.button4)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.buttonDiv)).perform(click())
        onView(withId(R.id.button8)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("5")))
    }

    @Test
    fun buttonSubDivTest() {
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.button4)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.buttonDiv)).perform(click())
        onView(withId(R.id.button8)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("-5")))
    }


    @Test
    fun restoreInstanceTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button7)).perform(click())
        device.setOrientationLeft()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("1337")))
    }

    @Test
    fun restoreInstanceTwiceTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button7)).perform(click())
        device.setOrientationLeft()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        device.setOrientationRight()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("1337")))
    }

    @Test
    fun someMoreRestoreInstanceTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button7)).perform(click())
        device.setOrientationLeft()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        device.setOrientationRight()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        device.setOrientationNatural()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        device.setOrientationRight()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        device.setOrientationLeft()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        device.setOrientationNatural()
        onView(withId(R.id.txt)).check(matches(withText("1337")))
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("1337")))
    }

    @Test
    fun divByZeroTest() {
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button8)).perform(click())
        onView(withId(R.id.buttonDiv)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("NaN")))
    }

    @Test
    fun buttonDotTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.buttonDot)).perform(click())
        onView(withId(R.id.button7)).perform(click())
        onView(withId(R.id.button7)).perform(click())
        onView(withId(R.id.button0)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("133.77")))
    }

    @Test
    fun changeOperatorTest() {
        onView(withId(R.id.button3)).perform(click())
        onView(withId(R.id.button8)).perform(click())
        onView(withId(R.id.buttonDot)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("38.")))
        onView(withId(R.id.buttonDiv)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("38/")))
        onView(withId(R.id.buttonMul)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("38*")))
        onView(withId(R.id.buttonMns)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("38-")))
        onView(withId(R.id.buttonPls)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("38+")))
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("38")))
    }

    @Test
    fun stupidAddMulExprTest() {
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonPls)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonMul)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("6")))
    }

    @Test
    fun stupidDivExprTest() {
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonDiv)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonDiv)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        onView(withId(R.id.buttonRes)).perform(click())
        onView(withId(R.id.txt)).check(matches(withText("0.5")))
    }
}