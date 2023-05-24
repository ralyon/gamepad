package com.ralyon.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.ralyon.gamepad.GamepadButton
import com.ralyon.gamepad.GamepadButtonType
import com.ralyon.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.gamepadMap.observe(this) { map ->
            logValues(map)
        }

        viewModel.buttonText.observe(this) {
            binding.startButton.text = getString(it)
        }

        binding.startButton.setOnClickListener {
            viewModel.onStartButtonPressed()
        }
    }

    private fun logValues(gamepadMap: Map<GamepadButtonType, GamepadButton>) {
        gamepadMap.forEach { (type, button) ->
            if (button.value == 0f) {
                return@forEach
            }

            val logText = if (!button.isStick && !button.isTrigger) {
                "${type}: ${button.value}"
            } else {
                type.toString()
            }

            val textView = TextView(this@MainActivity)
            textView.text = logText
            binding.logsContainer.addView(textView)
            binding.root.fullScroll(View.FOCUS_DOWN)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (viewModel.gamepad.shouldHandleKeyEvent(event)) {
            viewModel.gamepad.handleKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if (viewModel.gamepad.shouldHandleMotionEvent(event)) {
            viewModel.gamepad.handleMotionEvent(event)
            return true
        }
        return super.dispatchGenericMotionEvent(event)
    }

}