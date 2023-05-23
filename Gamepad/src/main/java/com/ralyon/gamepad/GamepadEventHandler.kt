package com.ralyon.gamepad

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import kotlin.math.abs
import com.ralyon.gamepad.GamepadButtonType.*

class GamepadEventHandler {

    var buttonMap: Map<GamepadButtonType, GamepadButton> = mapOf(
        BUTTON_START to GamepadButton(isButton = true),
        BUTTON_SELECT to GamepadButton(isButton = true),
        BUTTON_X to GamepadButton(isButton = true),
        BUTTON_Y to GamepadButton(isButton = true),
        BUTTON_B to GamepadButton(isButton = true),
        BUTTON_A to GamepadButton(isButton = true),
        BUTTON_R1 to GamepadButton(isButton = true),
        BUTTON_L1 to GamepadButton(isButton = true),
        STICK_LEFT_X to GamepadButton(isStick = true),
        STICK_LEFT_Y to GamepadButton(isStick = true),
        STICK_LEFT_BUTTON to GamepadButton(isButton = true),
        STICK_RIGHT_BUTTON to GamepadButton(isButton = true),
        STICK_RIGHT_X to GamepadButton(isStick = true),
        STICK_RIGHT_Y to GamepadButton(isStick = true),
        TRIGGER_LEFT to GamepadButton(isTrigger = true),
        TRIGGER_RIGHT to GamepadButton(isTrigger = true),
        DPAD_UP to GamepadButton(isButton = true),
        DPAD_DOWN to GamepadButton(isButton = true),
        DPAD_LEFT to GamepadButton(isButton = true),
        DPAD_RIGHT to GamepadButton(isButton = true)
    )
        private set

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

        buttonMap[STICK_LEFT_X]?.value = leftStickX
        buttonMap[STICK_LEFT_Y]?.value = leftStickY
        buttonMap[STICK_RIGHT_X]?.value = rightStickX
        buttonMap[STICK_RIGHT_Y]?.value = rightStickY
        buttonMap[TRIGGER_LEFT]?.value = triggerLeft
        buttonMap[TRIGGER_RIGHT]?.value = triggerRight
        buttonMap[DPAD_UP]?.value = if (dpadY == -1f) 1f else 0f
        buttonMap[DPAD_DOWN]?.value = if (dpadY == 1f) 1f else 0f
        buttonMap[DPAD_LEFT]?.value = if (dpadX == -1f) 1f else 0f
        buttonMap[DPAD_RIGHT]?.value = if (dpadX == 1f) 1f else 0f
    }

    fun handleButtonPressed(event: KeyEvent) {
        val action = event.action

        when (event.keyCode) {
            KeyEvent.KEYCODE_BUTTON_START -> buttonMap[BUTTON_START]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_SELECT -> buttonMap[BUTTON_SELECT]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_X -> buttonMap[BUTTON_X]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_A -> buttonMap[BUTTON_A]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_Y -> buttonMap[BUTTON_Y]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_B -> buttonMap[BUTTON_B]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_R1 -> buttonMap[BUTTON_R1]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_L1 -> buttonMap[BUTTON_L1]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_THUMBL -> buttonMap[STICK_LEFT_BUTTON]?.value =
                if (action == KeyEvent.ACTION_DOWN) 1f else 0f

            KeyEvent.KEYCODE_BUTTON_THUMBR -> buttonMap[STICK_RIGHT_BUTTON]?.value =
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