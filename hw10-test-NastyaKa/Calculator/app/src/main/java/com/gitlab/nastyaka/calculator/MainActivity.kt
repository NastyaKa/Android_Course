package com.gitlab.nastyaka.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.gitlab.nastyaka.calculator.databinding.ActivityMainBinding
import com.gitlab.nastyaka.calculator.parser.ExprParser
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var butClr: Button
    private lateinit var buf: ClipboardManager
    private lateinit var textik: TextView
    private var firstZero = false
    private var wasDot = false
    private var toClean = false
    private var curPos = 0

    private val handler = CoroutineExceptionHandler { _, exception ->
        Toast.makeText(this@MainActivity, exception.message, LENGTH_SHORT).show()
    }

    private fun setter(ch: Char) {
        if (toClean) {
            butClr.callOnClick()
        }
        if (ch.isDigit()) {
            if (ch == '0') {
                if (firstZero) {
                    return
                } else if (textik.text.isEmpty() || (textik.text.last() != '.'
                            && !textik.text.last().isDigit())
                ) {
                    firstZero = true
                }
            }
        } else if (ch != '.') {
            wasDot = false
            firstZero = false
            if (textik.text.isEmpty()) {
                textik.append("0")
            } else if (!textik.text.last().isDigit()) {
                textik.text = textik.text.dropLast(1)
            }
        } else {
            if (wasDot) {
                return
            } else {
                wasDot = true
            }
            firstZero = false
            if (textik.text.isEmpty() || !textik.text.last().isDigit()) {
                textik.append("0")
            }
        }

        textik.append(ch.toString())
    }

    private fun parseAns() = runBlocking {
        supervisorScope {
            launch(handler) {
                try {
                    textik.text = ExprParser().parseExpr(textik.text.toString())
                    wasDot = textik.text.contains('.')
                    firstZero = textik.text.startsWith("0")
                } catch (e: ArithmeticException) {
                    textik.text = getString(R.string.nan)
                    toClean = true
                } catch (e: Exception) {
                    textik.text = e.message
                    toClean = true
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textik = binding.txt
        textik.movementMethod = ScrollingMovementMethod()
        toClean = true

        buf = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.button0.apply {
            setOnClickListener {
                setter('0')
            }
        }
        binding.button1.apply {
            setOnClickListener {
                setter('1')
            }
        }
        binding.button2.apply {
            setOnClickListener {
                setter('2')
            }
        }
        binding.button3.apply {
            setOnClickListener {
                setter('3')
            }
        }
        binding.button4.apply {
            setOnClickListener {
                setter('4')
            }
        }
        binding.button5.apply {
            setOnClickListener {
                setter('5')
            }
        }
        binding.button6.apply {
            setOnClickListener {
                setter('6')
            }
        }
        binding.button7.apply {
            setOnClickListener {
                setter('7')
            }
        }
        binding.button8.apply {
            setOnClickListener {
                setter('8')
            }
        }
        binding.button9.apply {
            setOnClickListener {
                setter('9')
            }
        }
        binding.buttonDot.apply {
            setOnClickListener {
                setter('.')
            }
        }
        binding.buttonPls.apply {
            setOnClickListener {
                setter('+')
            }
        }
        binding.buttonMns.apply {
            setOnClickListener {
                setter('-')
            }
        }
        binding.buttonMul.apply {
            setOnClickListener {
                setter('*')
            }
        }
        binding.buttonDiv.apply {
            setOnClickListener {
                setter('/')
            }
        }
        butClr = binding.buttonClr.apply {
            setOnClickListener {
                wasDot = false
                firstZero = false
                toClean = false
                textik.text = ""
            }
        }
        binding.buttonRes.apply {
            setOnClickListener {
                if (toClean) {
                    butClr.callOnClick()
                }
                parseAns()
            }
        }
        binding.buttonMem.apply {
            setOnClickListener {
                buf.setPrimaryClip(ClipData.newPlainText(getString(R.string.app_name), textik.text))
                Toast.makeText(applicationContext, R.string.copytxt, LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TXT, textik.text.toString())
        outState.putBoolean(WDOT, wasDot)
        outState.putBoolean(FZERO, firstZero)
        outState.putInt(CPOS, curPos)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textik.text = savedInstanceState.getString(TXT)
        wasDot = savedInstanceState.getBoolean(WDOT)
        firstZero = savedInstanceState.getBoolean(FZERO)
        toClean = savedInstanceState.getBoolean(TOCLN)
        curPos = savedInstanceState.getInt(CPOS)
    }

    companion object {
        private const val TXT = "calc_hw.MainActivity.textik"
        private const val WDOT = "calc_hw.MainActivity.wasDot"
        private const val FZERO = "calc_hw.MainActivity.firstZero"
        private const val TOCLN = "calc_hw.MainActivity.toClean"
        private const val CPOS = "calc_hw.MainActivity.curPos"
    }
}