package com.unitx.signal_core.helper

import android.app.Activity
import android.content.Context
import android.os.Looper
import android.view.ViewGroup

internal fun ensureMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Signal must be called from the main thread."
    }
}

internal fun Activity.rootViewGroup(): ViewGroup? =
    window.decorView.rootView as? ViewGroup

internal fun Context.dp(dp: Int): Int =
    (dp * resources.displayMetrics.density).toInt()
