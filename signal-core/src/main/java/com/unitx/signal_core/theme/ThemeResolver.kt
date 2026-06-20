package com.unitx.signal_core.theme

import android.content.Context
import android.content.res.Configuration

internal class ThemeResolver(private val theme: ThemeConfig) {

    fun resolve(context: Context): ColorScheme {
        val isNight = (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val userScheme = if (isNight) theme.dark else theme.light
        return ThemeStaticDefaults.resolve(userScheme, isNight)
    }
}