package com.ralyon.gamepadexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralyon.gamepad.Gamepad
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _gamepadMap = MutableLiveData<Map<Int, Float>>()
    val gamepadMap: LiveData<Map<Int, Float>> = _gamepadMap

    private var gamepadMapJob: Job? = null

    fun startCollectingGamepadMap(gamepad: Gamepad, interval: Long) {
        gamepadMapJob = viewModelScope.launch {
            gamepad.getGamepadMapFlow(interval).collect {
                _gamepadMap.postValue(it)
            }
        }
    }

    fun stopCollectingGamepadMap() {
        gamepadMapJob?.cancel()
    }

}