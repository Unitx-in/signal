package com.unitx.signal

import android.app.Application
import com.unitx.signal_core.common.SnackPosition
import com.unitx.signal_core.launcher.Signal

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Signal.init(this) {
            snack {
                duration = 2500L
                dismissOnBackPress = false
                position = SnackPosition.Bottom
            }
        }
    }
}