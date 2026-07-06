package com.unitx.signal_core.helper

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.unitx.signal_core.activity.ActivityProvider

internal class BackPressHandler(private val activityProvider: ActivityProvider) {
    private var callback: OnBackPressedCallback? = null

    fun register(onBack: () -> Unit) {
        val activity = activityProvider.current() as? ComponentActivity ?: return
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { onBack() }
        }
        activity.onBackPressedDispatcher.addCallback(activity, callback!!)
    }

    fun unregister() {
        callback?.remove()
        callback = null
    }
}