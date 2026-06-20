package com.unitx.signal_core.theme

/**
 * Defines separate color schemes for light and dark mode.
 * Configured via [SignalConfig.theme].
 *
 * ```kotlin
 * Signal.createCore(this) {
 *     theme {
 *         light { snackBackground = Color.WHITE }
 *         dark { snackBackground = Color.BLACK }
 *     }
 * }
 * ```
 */
class ThemeConfig {

    /** Color scheme applied in light mode. */
    var light: ColorScheme = ColorScheme()

    /** Color scheme applied in dark mode. */
    var dark: ColorScheme = ColorScheme()

    /** Configures the light mode color scheme via [ColorSchemeBuilder]. */
    fun light(block: ColorSchemeBuilder.() -> Unit) {
        light = ColorSchemeBuilder().apply(block).build()
    }

    /** Configures the dark mode color scheme via [ColorSchemeBuilder]. */
    fun dark(block: ColorSchemeBuilder.() -> Unit) {
        dark = ColorSchemeBuilder().apply(block).build()
    }
}