package com.ralyon.gamepadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.ralyon.gamepad.Gamepad
import com.ralyon.gamepad.GamepadButton
import com.ralyon.gamepad.GamepadButtonType
import com.ralyon.gamepadexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var gamepad: Gamepad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gamepad = Gamepad()

        viewModel.gamepadMap.observe(this) { map ->
            logValues(map)
        }

        binding.startButton.setOnClickListener {
            if (!viewModel.isCollecting) {
                viewModel.startCollectingGamepadMap(gamepad, 100)
                binding.startButton.text = getString(R.string.stop)
            } else {
                viewModel.stopCollectingGamepadMap()
                binding.startButton.text = getString(R.string.start)
            }
        }
    }

    private fun logValues(gamepadMap: Map<GamepadButtonType, GamepadButton>) {
        gamepadMap.forEach {
            if (it.value.value == 0f) {
                return@forEach
            }

            val logText = if (it.value.isButton) {
                it.key.toString()
            } else {
                "${it.key}: ${it.value.value}"
            }

            val textView = TextView(this@MainActivity)
            textView.text = logText
            binding.logsContainer.addView(textView)
            binding.root.fullScroll(View.FOCUS_DOWN)
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