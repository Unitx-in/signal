package com.unitx.signal_core.common.theme

import android.content.Context
import android.content.res.Configuration

class SignalThemeResolver(private val theme: SignalTheme) {

    fun resolve(context: Context): SignalColorScheme {
        val isNight = (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val userScheme = if (isNight) theme.dark else theme.light
        return SignalDefaults.resolve(userScheme, isNight)
    }
}