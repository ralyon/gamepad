package com.ralyon.gamepadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.viewModels
import com.ralyon.gamepad.Gamepad

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var gamepad: Gamepad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gamepad = Gamepad()

        val rootView = findViewById<ScrollView>(R.id.root)
        val logsContainer = findViewById<LinearLayout>(R.id.logs_container)
        val startButton = findViewById<Button>(R.id.start_button)

        viewModel.gamepadMap.observe(this) { map ->
            GamepadHandler.handleInput(map) {
                val textView = TextView(this)
                textView.text = it
                logsContainer.addView(textView)
                rootView.fullScroll(View.FOCUS_DOWN)
            }
        }

        var isCollecting = false
        startButton.setOnClickListener {
            if (!isCollecting) {
                viewModel.startCollectingGamepadMap(gamepad, 100)
                startButton.text = getString(R.string.stop)
            } else {
                viewModel.stopCollectingGamepadMap()
                startButton.text = getString(R.string.start)
            }
            isCollecting = !isCollecting
        }

    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (gamepad.shouldHandleKeyEvent(event)) {
            gamepad.handleKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if (gamepad.shouldHandleMotionEvent(event)) {
            gamepad.handleMotionEvent(event)
            return true
        }
        return super.dispatchGenericMotionEvent(event)
    }

}