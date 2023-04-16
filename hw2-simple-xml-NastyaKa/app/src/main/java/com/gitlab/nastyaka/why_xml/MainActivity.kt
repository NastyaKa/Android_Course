package com.gitlab.nastyaka.why_xml

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var swithcher: Button
    private lateinit var login: Button
    private lateinit var mail: EditText
    private lateinit var password: EditText
    private lateinit var mistake: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swithcher = findViewById(R.id.pomenyai)
        login = findViewById(R.id.buttonLogin)
        mail = findViewById(R.id.email)
        password = findViewById(R.id.pswrd)
        mistake = findViewById(R.id.oshibochka)

        login.setOnClickListener {
            if (mail.text.isEmpty()) {
                mistake.text = getString(R.string.ne_tot_email)
            } else if (password.text.isEmpty()) {
                mistake.text = getString(R.string.ne_tot_password)
            } else {
                mistake.text = getString(R.string.prosto_oshibochka)
            }
        }

        swithcher.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else if ((applicationContext.resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            ) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login.callOnClick()
                true
            } else {
                false
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(MSTK, mistake.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mistake.text = savedInstanceState.getString(MSTK)
    }

    companion object {
        private const val MSTK = "calc_hw.MainActivity.mistake"
    }
}
