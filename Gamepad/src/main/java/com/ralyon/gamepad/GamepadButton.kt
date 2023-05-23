package com.ralyon.gamepad

data class GamepadButton(
    var value: Float = 0f,
    val isPressed: Boolean = false,
    val isButton: Boolean = false,
    val isStick: Boolean = false,
    val isTrigger: Boolean = false
)