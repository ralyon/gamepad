package com.ralyon.gamepadexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.ralyon.gamepad.Gamepad

class MainActivity : AppCompatActivity() {
    private lateinit var mGamepad: Gamepad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (mGamepad.shouldHandleKeyEvent(event)) {
            mGamepad.handleKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if (mGamepad.shouldHandleMotionEvent(event)) {
            mGamepad.handleMotionEvent(event)
            return true
        }
        return super.dispatchGenericMotionEvent(event)
    }
}