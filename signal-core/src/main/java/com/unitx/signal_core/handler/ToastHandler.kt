package com.unitx.signal_core.handler

import android.widget.Toast
import com.unitx.signal_core.common.SignalDuration
import com.unitx.signal_core.common.config.ToastConfig
import com.unitx.signal_core.core.SignalCore

class ToastHandler(
    private val host: SignalCore,
    private val defaultConfig: ToastConfig
) {

    private var currentToast: Toast? = null

    fun show(message: String) = show(message) {}

    fun show(message: String, block: ToastConfig.() -> Unit) {
        val config = ToastConfig().apply {
            duration = defaultConfig.duration
            type = defaultConfig.type
        }.apply(block)

        val activity = host.current() ?: return

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