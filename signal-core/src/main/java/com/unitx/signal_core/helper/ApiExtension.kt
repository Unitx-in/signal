package com.unitx.signal_core.helper

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Walks up the ContextWrapper chain to find the underlying Activity.
 * Needed because Compose's LocalContext.current is often a wrapped Context
 * (e.g. from a Theme wrapper), not the Activity itself.
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}