package com.unitx.signal_core.helper

import android.os.Looper

internal fun ensureMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Signal must be called from the main thread."
    }
}