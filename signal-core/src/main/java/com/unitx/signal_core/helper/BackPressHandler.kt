package com.unitx.signal_core.helper

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback

internal class BackPressHandler {
    private var callback: OnBackPressedCallback? = null

    /**
     * Registers [onBack] to run when the system back button/gesture is triggered while
     * [activity] is in the foreground. No-ops silently if [activity] isn't a
     * [ComponentActivity] (required for [androidx.activity.OnBackPressedDispatcher]).
     */
    fun register(activity: Activity, onBack: () -> Unit) {
        val componentActivity = activity as? ComponentActivity ?: return
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { onBack() }
        }
        componentActivity.onBackPressedDispatcher.addCallback(componentActivity, callback!!)
    }

    fun unregister() {
        callback?.remove()
        callback = null
    }
}