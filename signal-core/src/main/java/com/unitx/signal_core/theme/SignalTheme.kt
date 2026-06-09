package com.unitx.signal_core.theme

class SignalTheme {
    var light: SignalColorScheme = SignalColorScheme()
    var dark: SignalColorScheme = SignalColorScheme()

    fun light(block: SignalColorSchemeBuilder.() -> Unit) {
        light = SignalColorSchemeBuilder().apply(block).build()
    }

    fun dark(block: SignalColorSchemeBuilder.() -> Unit) {
        dark = SignalColorSchemeBuilder().apply(block).build()
    }
}