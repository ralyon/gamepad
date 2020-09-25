package com.ralyon.gamepadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import com.ralyon.gamepad.Gamepad
import com.ralyon.gamepad.GamepadButton

class MainActivity : AppCompatActivity() {
    private val TAG = "GamepadExample"

    private lateinit var gamepad: Gamepad
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gamepad = Gamepad()

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                handleGamepadInput(gamepad.getGamepadMap())
                handler.postDelayed(this, 100)
            }
        }
    }

    fun handleGamepadInput(gamepadMap: Map<Int, Float>) {
        if (gamepadMap[GamepadButton.DPAD_UP] == 1f) {
            Log.i(TAG, "DPAD_UP")
        }

        if (gamepadMap[GamepadButton.DPAD_DOWN] == 1f) {
            Log.i(TAG, "DPAD_DOWN")
        }

        if (gamepadMap[GamepadButton.DPAD_LEFT] == 1f) {
            Log.i(TAG, "DPAD_LEFT")
        }

        if (gamepadMap[GamepadButton.DPAD_RIGHT] == 1f) {
            Log.i(TAG, "DPAD_RIGHT")
        }

        val stickLeftX = gamepadMap[GamepadButton.STICK_LEFT_X]
        if (stickLeftX != 0f) {
            Log.i(TAG, "STICK_LEFT_X: $stickLeftX")
        }

        val stickLeftY = gamepadMap[GamepadButton.STICK_LEFT_Y]
        if (stickLeftY!! != 0f) {
            Log.i(TAG, "STICK_LEFT_Y: $stickLeftY")
        }

        val stickRightX = gamepadMap[GamepadButton.STICK_RIGHT_X]
        if (stickRightX!! != 0f) {
            Log.i(TAG, "STICK_RIGHT_X: $stickRightX")
        }

        val stickRightY = gamepadMap[GamepadButton.STICK_RIGHT_Y]
        if (stickRightY!! != 0f) {
            Log.i(TAG, "STICK_RIGHT_Y: $stickRightY")
        }

        if (gamepadMap[GamepadButton.BUTTON_A] == 1f) {
            Log.i(TAG, "BUTTON_A")
        }

        if (gamepadMap[GamepadButton.BUTTON_B] == 1f) {
            Log.i(TAG, "BUTTON_B")
        }

        if (gamepadMap[GamepadButton.BUTTON_X] == 1f) {
            Log.i(TAG, "BUTTON_X")
        }

        if (gamepadMap[GamepadButton.BUTTON_Y] == 1f) {
            Log.i(TAG, "BUTTON_Y")
        }

        if (gamepadMap[GamepadButton.BUTTON_R1] == 1f) {
            Log.i(TAG, "BUTTON_R1")
        }

        if (gamepadMap[GamepadButton.BUTTON_L1] == 1f) {
            Log.i(TAG, "BUTTON_L1")
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

    override fun onResume() {
        handler.post(runnable)
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }
}