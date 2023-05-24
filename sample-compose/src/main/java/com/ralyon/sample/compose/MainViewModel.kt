package com.ralyon.sample.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralyon.gamepad.Gamepad
import com.ralyon.gamepad.GamepadButton
import com.ralyon.gamepad.GamepadButtonType
import com.ralyon.gamepad.asFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _buttonText = MutableStateFlow(R.string.start)
    val buttonText = _buttonText.asStateFlow()

    private val _gamepadLog = MutableStateFlow<List<String>>(emptyList())
    val gamepadLog = _gamepadLog.asStateFlow()

    var gamepad = Gamepad()
        private set

    private var isCollecting = false
    private var gamepadMapJob: Job? = null

    fun onStartButtonPressed() {
        if (isCollecting) {
            stopCollectingGamepadMap()
        } else {
            startCollectingGamepadMap()
        }
    }

    private fun startCollectingGamepadMap() {
        gamepadMapJob = viewModelScope.launch {
            isCollecting = true
            _buttonText.value = R.string.stop
            gamepad.gamepadMap.asFlow(INTERVAL).collect { gamepadMap ->
                val logs = gamepadMap.filterNot { it.value.value == 0f }.map { it.toLog() }
                _gamepadLog.value = gamepadLog.value.plus(logs)
            }
        }
    }

    private fun stopCollectingGamepadMap() {
        gamepadMapJob?.cancel()
        isCollecting = false
        _buttonText.value = R.string.start
    }

    private fun Map.Entry<GamepadButtonType, GamepadButton>.toLog(): String {
        val (type, button) = this
        return if (button.isStick || button.isTrigger) {
            "${type}: ${button.value}"
        } else {
            type.toString()
        }
    }

    companion object {
        private const val INTERVAL = 100L
    }
}