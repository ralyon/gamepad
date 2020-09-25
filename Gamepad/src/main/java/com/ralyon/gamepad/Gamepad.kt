package com.ralyon.gamepad

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent

class Gamepad {

    private var mEventHandler: GamepadEventHandler = GamepadEventHandler()

    fun shouldHandleMotionEvent(event: MotionEvent): Boolean {
        // Check that the event came from a game controller
        return event.source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK && event.action == MotionEvent.ACTION_MOVE
    }

    fun handleMotionEvent(event: MotionEvent) {
        // Process all historical movement samples in the batch
        val historySize = event.historySize

        // Process the movements starting from the
        // earliest historical position in the batch
        for (i in 0 until historySize) {
            // Process the event at historical position i
            mEventHandler.processJoystickInput(event, i)

        }

        // Process the current movement sample in the batch (position -1)
        mEventHandler.processJoystickInput(event, -1)
    }

    fun shouldHandleKeyEvent(event: KeyEvent): Boolean {
        return event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
    }

    fun handleKeyEvent(event: KeyEvent) {
        if (event.repeatCount == 0) {
            mEventHandler.handleButtonPressed(event)
        }
    }

    fun getGamepadMap(): Map<Int, Float> {
        return mEventHandler.getButtonList()
    }

}
