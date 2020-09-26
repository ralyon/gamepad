package com.ralyon.gamepad

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import kotlin.math.abs

class GamepadEventHandler {

    private var mButtonList = mutableMapOf<Int, Float>()

    init {
        mButtonList[GamepadButton.BUTTON_START] = 0f
        mButtonList[GamepadButton.BUTTON_SELECT] = 0f
        mButtonList[GamepadButton.BUTTON_X] = 0f
        mButtonList[GamepadButton.BUTTON_Y] = 0f
        mButtonList[GamepadButton.BUTTON_B] = 0f
        mButtonList[GamepadButton.BUTTON_A] = 0f
        mButtonList[GamepadButton.BUTTON_R1] = 0f
        mButtonList[GamepadButton.BUTTON_L1] = 0f
        mButtonList[GamepadButton.STICK_LEFT_X] = 0f
        mButtonList[GamepadButton.STICK_LEFT_Y] = 0f
        mButtonList[GamepadButton.STICK_LEFT_BUTTON] = 0f
        mButtonList[GamepadButton.STICK_RIGHT_X] = 0f
        mButtonList[GamepadButton.STICK_RIGHT_Y] = 0f
        mButtonList[GamepadButton.STICK_RIGHT_BUTTON] = 0f
        mButtonList[GamepadButton.TRIGGER_LEFT] = 0f
        mButtonList[GamepadButton.TRIGGER_RIGHT] = 0f
        mButtonList[GamepadButton.DPAD_UP] = 0f
        mButtonList[GamepadButton.DPAD_DOWN] = 0f
        mButtonList[GamepadButton.DPAD_LEFT] = 0f
        mButtonList[GamepadButton.DPAD_RIGHT] = 0f
    }

    fun getButtonList(): MutableMap<Int, Float> {
        return mButtonList
    }

    fun processJoystickInput(event: MotionEvent, historyPos: Int) {
        val inputDevice = event.device

        // Left control stick and hat
        // Horizontal distance
        val leftStickX = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos)
        val dpadX = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_X, historyPos)

        // Vertical distance
        val leftStickY = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos)
        val dpadY = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_HAT_Y, historyPos)

        // Right control stick
        // Horizontal distance
        val rightStickX = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos)
        // Vertical distance
        val rightStickY = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos)

        // Shoulder triggers
        // Left
        val triggerLeft = if (historyPos < 0)
            event.getAxisValue(MotionEvent.AXIS_BRAKE)
        else
            event.getHistoricalAxisValue(MotionEvent.AXIS_BRAKE, historyPos)
        // Right
        val triggerRight = if (historyPos < 0)
            event.getAxisValue(MotionEvent.AXIS_GAS)
        else
            event.getHistoricalAxisValue(MotionEvent.AXIS_GAS, historyPos)

        mButtonList[GamepadButton.STICK_LEFT_X] = leftStickX
        mButtonList[GamepadButton.STICK_LEFT_Y] = leftStickY
        mButtonList[GamepadButton.STICK_RIGHT_X] = rightStickX
        mButtonList[GamepadButton.STICK_RIGHT_Y] = rightStickY
        mButtonList[GamepadButton.TRIGGER_LEFT] = triggerLeft
        mButtonList[GamepadButton.TRIGGER_RIGHT] = triggerRight
        mButtonList[GamepadButton.DPAD_UP] = if (dpadY == -1f) 1f else 0f
        mButtonList[GamepadButton.DPAD_DOWN] = if (dpadY == 1f) 1f else 0f
        mButtonList[GamepadButton.DPAD_LEFT] = if (dpadX == -1f) 1f else 0f
        mButtonList[GamepadButton.DPAD_RIGHT] = if (dpadX == 1f) 1f else 0f
    }

    fun handleButtonPressed(event: KeyEvent) {
        val action = event.action

        when (event.keyCode) {
            KeyEvent.KEYCODE_BUTTON_START ->
                mButtonList[GamepadButton.BUTTON_START] =
                    if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_SELECT ->
                mButtonList[GamepadButton.BUTTON_SELECT] =
                    if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_X ->
                mButtonList[GamepadButton.BUTTON_X] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_A ->
                mButtonList[GamepadButton.BUTTON_A] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_Y ->
                mButtonList[GamepadButton.BUTTON_Y] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_B ->
                mButtonList[GamepadButton.BUTTON_B] = if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_R1 ->
                mButtonList[GamepadButton.BUTTON_R1] =
                    if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_L1 ->
                mButtonList[GamepadButton.BUTTON_L1] =
                    if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_THUMBL ->
                mButtonList[GamepadButton.STICK_LEFT_BUTTON] =
                    if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_THUMBR ->
                mButtonList[GamepadButton.STICK_RIGHT_BUTTON] =
                    if (action == KeyEvent.ACTION_DOWN) 1f else 0f
        }
    }

    private fun getCenteredAxis(
        event: MotionEvent,
        device: InputDevice,
        axis: Int,
        historyPos: Int
    ): Float {
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