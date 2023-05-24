package com.ralyon.sample.compose

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ralyon.sample.compose.ui.theme.GamepadTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamepadTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        Scaffold(
            topBar = { MainTopBar() },
            content = { MainContent(it) }
        )
    }

    @Composable
    private fun MainTopBar() {
        TopAppBar(contentPadding = PaddingValues(horizontal = 16.dp)) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }

    @Composable
    private fun MainContent(contentPadding: PaddingValues) {
        val buttonText = viewModel.buttonText.collectAsStateWithLifecycle()
        val gamepadLog = viewModel.gamepadLog.collectAsStateWithLifecycle()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = { viewModel.onStartButtonPressed() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = stringResource(buttonText.value).uppercase())
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    items(gamepadLog.value) {
                        Text(it)
                    }
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun MainScreenPreview() {
        GamepadTheme {
            MainScreen()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (viewModel.gamepad.shouldHandleKeyEvent(event)) {
            viewModel.gamepad.handleKeyEvent(event)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if (viewModel.gamepad.shouldHandleMotionEvent(event)) {
            viewModel.gamepad.handleMotionEvent(event)
            return true
        }
        return super.dispatchGenericMotionEvent(event)
    }
}