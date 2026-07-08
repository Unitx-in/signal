package com.unitx.signal

import android.app.Application
import android.graphics.Color
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.main.Signal
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.helper.color
import com.unitx.signal_core.queue.QueueStrategy

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Signal.createCore(this) {
            setQueueStrategy(QueueStrategy.Independent)
            theme {
                light {
                    snackBackground = Color.WHITE
                    toastBackground = applicationContext.color(R.color.white)
                }
                dark {
                    snackBackground = Color.BLACK
//                    toastBackground = ContextCompat.getColor(applicationContext, R.color.black)
                }
            }
            snack {
                duration = 2500L
                dismissOnBackPress = true
                position = SnackPosition.Top
            }
            toast {
                position = ToastPosition.Bottom
            }
        }
    }
}