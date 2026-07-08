package com.unitx.signal_core.helper

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

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

/**
 * Resolves a color resource to its actual `@ColorInt` ARGB value.
 *
 * Shorthand for [ContextCompat.getColor], intended for call sites (like theme
 * configuration) that need a resolved color int rather than a resource ID —
 * passing a raw `R.color.xxx` resource ID where a `@ColorInt` is expected will
 * silently produce a wrong/garbled color instead of a compile error.
 *
 * Example:
 * ```
 * lightConfig.setToastBackground(applicationContext.color(R.color.white))
 * ```
 */
@ColorInt
fun Context.color(@ColorRes res: Int): Int = ContextCompat.getColor(this, res)