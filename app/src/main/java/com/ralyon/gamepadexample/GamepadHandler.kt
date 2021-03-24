package com.ralyon.gamepadexample

import com.ralyon.gamepad.GamepadButton

object GamepadHandler {

    /**
     * Reads the gamepad buttons values and logs if a button is pressed
     * @param gamepadMap the gamepad buttons status map
     */
    fun handleInput(gamepadMap: Map<Int, Float>, callback: (String) -> Unit) {
        if (gamepadMap[GamepadButton.DPAD_UP] == 1f) {
            callback("DPAD_UP")
        }

        if (gamepadMap[GamepadButton.DPAD_DOWN] == 1f) {
            callback("DPAD_DOWN")
        }

        if (gamepadMap[GamepadButton.DPAD_LEFT] == 1f) {
            callback("DPAD_LEFT")
        }

        if (gamepadMap[GamepadButton.DPAD_RIGHT] == 1f) {
            callback("DPAD_RIGHT")
        }

        val stickLeftX = gamepadMap[GamepadButton.STICK_LEFT_X]
        if (stickLeftX != 0f) {
            callback("STICK_LEFT_X: $stickLeftX")
        }

        val stickLeftY = gamepadMap[GamepadButton.STICK_LEFT_Y]
        if (stickLeftY != 0f) {
            callback("STICK_LEFT_Y: $stickLeftY")
        }

        if (gamepadMap[GamepadButton.STICK_LEFT_BUTTON] == 1f) {
            callback("STICK_LEFT_BUTTON")
        }

        val stickRightX = gamepadMap[GamepadButton.STICK_RIGHT_X]
        if (stickRightX != 0f) {
            callback("STICK_RIGHT_X: $stickRightX")
        }

        val stickRightY = gamepadMap[GamepadButton.STICK_RIGHT_Y]
        if (stickRightY != 0f) {
            callback("STICK_RIGHT_Y: $stickRightY")
        }

        if (gamepadMap[GamepadButton.STICK_RIGHT_BUTTON] == 1f) {
            callback("STICK_RIGHT_BUTTON")
        }

        val triggerLeft = gamepadMap[GamepadButton.TRIGGER_LEFT]
        if (triggerLeft != 0f) {
            callback("TRIGGER_LEFT: $triggerLeft")
        }

        val triggerRight = gamepadMap[GamepadButton.TRIGGER_RIGHT]
        if (triggerRight != 0f) {
            callback("TRIGGER_RIGHT: $triggerRight")
        }

        if (gamepadMap[GamepadButton.BUTTON_A] == 1f) {
            callback("BUTTON_A")
        }

        if (gamepadMap[GamepadButton.BUTTON_B] == 1f) {
            callback("BUTTON_B")
        }

        if (gamepadMap[GamepadButton.BUTTON_X] == 1f) {
            callback("BUTTON_X")
        }

        if (gamepadMap[GamepadButton.BUTTON_Y] == 1f) {
            callback("BUTTON_Y")
        }

        if (gamepadMap[GamepadButton.BUTTON_R1] == 1f) {
            callback("BUTTON_R1")
        }

        if (gamepadMap[GamepadButton.BUTTON_L1] == 1f) {
            callback("BUTTON_L1")
        }

        if (gamepadMap[GamepadButton.BUTTON_START] == 1f) {
            callback("BUTTON_START")
        }

        if (gamepadMap[GamepadButton.BUTTON_SELECT] == 1f) {
            callback("BUTTON_SELECT")
        }
    }

}