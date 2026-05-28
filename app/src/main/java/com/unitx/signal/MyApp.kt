package com.unitx.signal

import android.app.Application
import android.graphics.Color
import com.unitx.signal_core.common.type.SnackPosition
import com.unitx.signal_core.launcher.Signal
import androidx.core.graphics.toColorInt
import com.unitx.signal_core.common.type.ToastPosition

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Signal.createCore(this) {
            theme {
                light {
                    snackBackground = R.color.white
//                    toastBackground = R.color.white
                }
                dark {
                    snackBackground = R.color.black
//                    toastBackground = R.color.black
                }
            }
            snack {
                duration = 2500L
                dismissOnBackPress = false
                position = SnackPosition.Top
            }
            toast {
                position = ToastPosition.Bottom
            }
        }
    }
}