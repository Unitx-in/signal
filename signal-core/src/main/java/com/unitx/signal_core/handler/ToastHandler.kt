package com.unitx.signal_core.handler

import android.widget.Toast
import com.unitx.signal_core.common.type.SignalDuration
import com.unitx.signal_core.common.config.base.ToastConfig
import com.unitx.signal_core.provider.ActivityProvider

class ToastHandler(
    private val activityProvider: ActivityProvider,
    private val defaultConfig: ToastConfig
) {

    private var currentToast: Toast? = null

    fun show(message: String) = show(message) {}

    fun show(message: String, block: ToastConfig.() -> Unit) {
        val config = ToastConfig().apply(block)
        val activity = activityProvider.current() ?: return

        currentToast?.cancel()
        currentToast = Toast.makeText(
            activity,
            message,
            when (config.duration) {
                is SignalDuration.Long -> Toast.LENGTH_LONG
                else -> Toast.LENGTH_SHORT
            }
        ).also { it.show() }
    }

    fun dismiss() {
        currentToast?.cancel()
        currentToast = null
    }
}