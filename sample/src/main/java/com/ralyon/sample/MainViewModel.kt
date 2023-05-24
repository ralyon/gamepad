package com.ralyon.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralyon.gamepad.Gamepad
import com.ralyon.gamepad.GamepadButton
import com.ralyon.gamepad.GamepadButtonType
import com.ralyon.gamepad.asFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _gamepadMap = MutableLiveData<Map<GamepadButtonType, GamepadButton>>()
    val gamepadMap: LiveData<Map<GamepadButtonType, GamepadButton>> = _gamepadMap

    private val _buttonText = MutableLiveData(R.string.start)
    val buttonText: LiveData<Int> = _buttonText

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
            gamepad.gamepadMap.asFlow(INTERVAL).collect {
                _gamepadMap.value = it
            }
        }
    }

    private fun stopCollectingGamepadMap() {
        gamepadMapJob?.cancel()
        isCollecting = false
        _buttonText.value = R.string.start
    }

    companion object {
        private const val INTERVAL = 100L
    }
}