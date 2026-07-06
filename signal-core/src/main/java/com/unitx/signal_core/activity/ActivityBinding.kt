package com.unitx.signal_core.activity

import android.app.Activity

internal class ActivityBinding internal constructor(
    private val provider: ActivityProvider,
    private val activity: Activity,
    private val listener: () -> Unit
) {
    private var active = true

    val isActive: Boolean
        get() = active

    fun unbind() {
        if (!active) return
        active = false
        provider.removeOnDestroyListener(activity, listener)
    }
}