package com.gitlab.nastyaka.calculator

import org.junit.Test
import com.gitlab.nastyaka.calculator.parser.ExprParser
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun additionTest() {
        assertEquals("5", ExprParser().parseExpr("2+3"))
    }

    @Test
    fun subtractionTest() {
        assertEquals("-17", ExprParser().parseExpr("3-20"))
    }

    @Test
    fun negativeConstantTest() {
        assertEquals("-228", ExprParser().parseExpr("-228"))
    }

    @Test
    fun multiplyNegativePositiveTest() {
        assertEquals("-300", ExprParser().parseExpr("-20*15"))
    }

    @Test
    fun multiplyPositivePositiveTest() {
        assertEquals("500", ExprParser().parseExpr("20*25"))
    }

    @Test
    fun multiplyDoubleTest() {
        assertEquals("6.9", ExprParser().parseExpr("2.3*3"))
    }

    @Test
    fun divideNegativePositiveTest() {
        assertEquals("-2", ExprParser().parseExpr("-20/10"))
    }

    @Test
    fun dividePositivePositiveTest() {
        assertEquals("4", ExprParser().parseExpr("100/25"))
    }

    @Test
    @Throws(ArithmeticException::class)
    fun divideByZeroTest() {
        assertThatThrownBy { ExprParser().parseExpr("9/0") }
    }

    @Test
    fun divideDoubleTest() {
        assertEquals("0.25", ExprParser().parseExpr("1/4"))
    }
}