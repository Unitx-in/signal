package com.unitx.signal_core.handler.snack

import android.os.Handler
import android.os.Looper

class SnackDismissScheduler {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var dismissRunnable: Runnable? = null

    fun schedule(duration: Long, onDismiss: () -> Unit) {
        cancel()
        dismissRunnable = Runnable { onDismiss() }
        mainHandler.postDelayed(dismissRunnable!!, duration)
    }

    fun cancel() {
        dismissRunnable?.let { mainHandler.removeCallbacks(it) }
        dismissRunnable = null
    }
}