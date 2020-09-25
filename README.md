# Gamepad
A small Android library that helps you easily support game controllers in your app


The library goal is to simplify the implementation of bluetooth game controllers (such as the Microsoft Xbox One Controller) by providing instant access to the gamepad button state.


How to use
---------

First, you need to instantiate a ``Gamepad`` object:
```kotlin
val gamepad = Gamepad()
```

Then override ``dispatchKeyEvent(android.view.KeyEvent)`` and ``dispatchGenericMotionEvent(android.view.MotionEvent)`` methods in your ``Activity``:

```kotlin
override fun dispatchKeyEvent(event: KeyEvent): Boolean {
    if (gamepad.shouldHandleKeyEvent(event)) {
        gamepad.handleKeyEvent(event)
        return true
    }
    return super.dispatchKeyEvent(event)
}

override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
    if (gamepad.shouldHandleMotionEvent(event)) {
        gamepad.handleMotionEvent(event)
        return true
    }
    return super.dispatchGenericMotionEvent(event)
}
```

Now you can call ``getGamepadMap()`` any time you need to know the state of the gamepad buttons. The method returns a ``MutableMap<Int, Float>`` where the key is the button identifier and the value change from 0f to 1f if the button is pressed.

For example:
```kotlin
val map = gamepad.getGamepadMap()
if (map[GamepadButton.BUTTON_X] == 1f) {
  // the button X is pressed
} else {
  // the button X is not pressed
}
```

See the example app to know more.
