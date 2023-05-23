package com.ralyon.gamepad

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Get the current gamepad buttons status map as a coroutine Flow
 * @return The gamepad buttons status map Flow
 */
fun Map<GamepadButtonType, GamepadButton>.asFlow(
    interval: Long
): Flow<Map<GamepadButtonType, GamepadButton>> = flow {
    while (true) {
        emit(this@asFlow)
        delay(interval)
    }
}