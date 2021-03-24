package com.ralyon.gamepad

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Gamepad {

    private var mEventHandler: GamepadEventHandler = GamepadEventHandler()

    /**
     * Checks if the MotionEvent comes from a game controller
     * @param event The event to check
     */
    fun shouldHandleMotionEvent(event: MotionEvent): Boolean {
        return event.source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK && event.action == MotionEvent.ACTION_MOVE
    }

    /**
     * Pass the MotionEvent to the event handler
     * @param event The event to pass
     */
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

    /**
     * Check if the KeyEvent comes from a game controller
     * @param event The event to check
     */
    fun shouldHandleKeyEvent(event: KeyEvent): Boolean {
        return event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD
    }

    /**
     * Pass the KeyEvent to the event handler
     * @param event The event to pass
     */
    fun handleKeyEvent(event: KeyEvent) {
        if (event.repeatCount == 0) {
            mEventHandler.handleButtonPressed(event)
        }
    }

    /**
     * Get the current gamepad buttons status map from the event handler
     * @return The gamepad buttons status map
     */
    fun getGamepadMap(): Map<Int, Float> {
        return mEventHandler.getButtonList()
    }

    /**
     * Get the current gamepad buttons status map as a coroutine Flow
     * @return The gamepad buttons status map Flow
     */
    suspend fun getGamepadMapFlow(interval: Long): Flow<Map<Int, Float>> = flow {
        while (true) {
            val currentMap = getGamepadMap()
            emit(currentMap)
            delay(interval)
        }
    }

}
