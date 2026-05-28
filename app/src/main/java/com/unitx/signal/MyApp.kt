package com.unitx.signal

import android.app.Application
import android.graphics.Color
import com.unitx.signal_core.common.type.SnackPosition
import com.unitx.signal_core.launcher.Signal
import androidx.core.graphics.toColorInt

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Signal.createCore(this) {
            theme {
                light { snackBackground = Color.WHITE }
                dark { snackBackground = "#000000".toColorInt() }
            }
            snack {
                duration = 2500L
                dismissOnBackPress = false
                position = SnackPosition.Top
            }
        }
    }
}