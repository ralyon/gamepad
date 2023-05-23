package com.ralyon.gamepadexample

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

    var isCollecting = false
        private set

    private var gamepadMapJob: Job? = null

    fun startCollectingGamepadMap(gamepad: Gamepad, interval: Long) {
        gamepadMapJob = viewModelScope.launch {
            isCollecting = true
            gamepad.gamepadMap.asFlow(interval).collect {
                _gamepadMap.postValue(it)
            }
        }
    }

    fun stopCollectingGamepadMap() {
        gamepadMapJob?.cancel()
        isCollecting = false
    }

}