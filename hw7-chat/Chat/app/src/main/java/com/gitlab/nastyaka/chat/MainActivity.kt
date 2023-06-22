package com.gitlab.nastyaka.chat

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var lastOrientation: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            lastOrientation = resources.configuration.orientation
        }
    }

    override fun onStart() {
        super.onStart()
        val currentOrientation = resources.configuration.orientation
        if (currentOrientation != lastOrientation) {
            try {
                supportFragmentManager.popBackStackImmediate(
                    "chat",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            } catch (e: NullPointerException) {
                Log.e(e.javaClass.simpleName, e.message.toString())
            }
            lastOrientation = currentOrientation
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        lastOrientation = savedInstanceState.getInt("orientation")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("orientation", lastOrientation)
    }
}