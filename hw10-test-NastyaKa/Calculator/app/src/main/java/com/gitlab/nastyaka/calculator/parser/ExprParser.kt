package com.gitlab.nastyaka.calculator.parser

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class ExprParser {
    private var curPos = 0
    private var loc = DecimalFormatSymbols(Locale.US)
    private var strForm = "#.###############"
    private var nf = DecimalFormat(strForm, loc)

    fun parseExpr(str: String): String {
        if (str.isEmpty()) {
            return "0"
        }
        curPos = 0
        try {
            return if (!str.last().isDigit()) {
                nf.format(parsePlsMns(str.dropLast(1)))
            } else {
                nf.format(parsePlsMns(str))
            }
        } catch (e: ArithmeticException) {
            throw e
        } catch (e: java.lang.NumberFormatException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }

    private fun parsePlsMns(str: String): BigDecimal {
        var first: BigDecimal
        try {
            first = parseMulDiv(str)
        } catch (e: ArithmeticException) {
            throw e
        } catch (e: java.lang.NumberFormatException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
        while (curPos < str.length) {
            first = if (str[curPos] == '+') {
                curPos++
                first.add(parseMulDiv(str))
            } else if (str[curPos] == '-') {
                curPos++
                first.subtract(parseMulDiv(str))
            } else {
                break
            }
        }
        return first
    }

    private fun parseMulDiv(str: String): BigDecimal {
        var first: BigDecimal
        try {
            first = parseConst(str)
        } catch (e: ArithmeticException) {
            throw e
        } catch (e: java.lang.NumberFormatException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
        while (curPos < str.length) {
            first = if (str[curPos] == '*') {
                curPos++
                first.multiply(parseConst(str))
            } else if (str[curPos] == '/') {
                curPos++
                try {
                    first.divide(parseConst(str), 14, RoundingMode.HALF_UP)
                } catch (e: ArithmeticException) {
                    throw e
                }
            } else {
                break
            }
        }
        return first
    }

    private fun parseConst(str: String): BigDecimal {
        var cnst = ""
        var isNeg = false
        if (str[curPos] == '-') {
            isNeg = true
            cnst += "-"
            curPos++
        }
        while (curPos < str.length && (str[curPos].isDigit() || str[curPos] == '.')) {
            cnst += str[curPos++]
        }
        if (cnst.isEmpty() || cnst.length == 1 && isNeg) {
            throw ArithmeticException("Incorrect input")
        } else {
            try {
                return cnst.toBigDecimal().setScale(14, RoundingMode.HALF_UP)
            } catch (e: java.lang.NumberFormatException) {
                throw e
            }
        }
    }
}