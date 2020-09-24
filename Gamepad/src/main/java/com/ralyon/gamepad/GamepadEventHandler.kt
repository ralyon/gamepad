package com.ralyon.gamepad

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import kotlin.math.abs

class GamepadEventHandler {

    private var mButtonList = mutableMapOf<GamepadButton, Float>()

    init {
        mButtonList[GamepadButton.BUTTON_X] = 0f
        mButtonList[GamepadButton.BUTTON_Y] = 0f
        mButtonList[GamepadButton.BUTTON_B] = 0f
        mButtonList[GamepadButton.BUTTON_A] = 0f
        mButtonList[GamepadButton.BUTTON_R1] = 0f
        mButtonList[GamepadButton.BUTTON_L1] = 0f
        mButtonList[GamepadButton.STICK_LEFT_X] = 0f
        mButtonList[GamepadButton.STICK_LEFT_Y] = 0f
        mButtonList[GamepadButton.STICK_RIGHT_X] = 0f
        mButtonList[GamepadButton.STICK_RIGHT_Y] = 0f
        mButtonList[GamepadButton.TRIGGER_LEFT] = 0f
        mButtonList[GamepadButton.TRIGGER_RIGHT] = 0f
    }

    fun getButtonList(): MutableMap<GamepadButton, Float> {
        return mButtonList
    }

    fun processJoystickInput(event: MotionEvent, historyPos: Int) {
        val inputDevice = event.device

        // Left control stick and hat
        // Horizontal distance
        var lx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos)
        if (lx == 0f) {
            lx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_X, historyPos)
        }
        // Vertical distance
        var ly = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos)
        if (ly == 0f) {
            ly = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_Y, historyPos)
        }

        // Right control stick
        // Horizontal distance
        val rx = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos)
        // Vertical distance
        val ry = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos)

        // Shoulder triggers
        // Left
        val lt = if (historyPos < 0)
            event.getAxisValue(MotionEvent.AXIS_BRAKE)
        else
            event.getHistoricalAxisValue(MotionEvent.AXIS_BRAKE, historyPos)
        // Right
        val rt = if (historyPos < 0)
            event.getAxisValue(MotionEvent.AXIS_GAS)
        else
            event.getHistoricalAxisValue(MotionEvent.AXIS_GAS, historyPos)

        mButtonList[GamepadButton.STICK_LEFT_X] = lx * 100
        mButtonList[GamepadButton.STICK_LEFT_Y] = ly * 100
        mButtonList[GamepadButton.STICK_RIGHT_X] = rx * 100
        mButtonList[GamepadButton.STICK_RIGHT_Y] = ry * 100
        mButtonList[GamepadButton.TRIGGER_LEFT] = lt * 100
        mButtonList[GamepadButton.TRIGGER_RIGHT] = rt * 100
    }

    fun handleButtonPressed(event: KeyEvent) {
        val action = event.action

        when (event.keyCode) {
            KeyEvent.KEYCODE_BUTTON_X ->
                mButtonList[GamepadButton.BUTTON_X] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_A ->
                mButtonList[GamepadButton.BUTTON_A] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_Y ->
                mButtonList[GamepadButton.BUTTON_Y] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_B ->
                mButtonList[GamepadButton.BUTTON_B] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_R1 ->
                mButtonList[GamepadButton.BUTTON_R1] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_L1 ->
                mButtonList[GamepadButton.BUTTON_L1] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f
        }
    }

    private fun getCenteredAxis(event: MotionEvent, device: InputDevice, axis: Int, historyPos: Int): Float {
        val range = device.getMotionRange(axis, event.source)

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            val flat = range.flat
            val value = if (historyPos < 0)
                event.getAxisValue(axis)
            else
                event.getHistoricalAxisValue(axis, historyPos)

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (abs(value) > flat) {
                return value
            }
        }
        return 0f
    }
}