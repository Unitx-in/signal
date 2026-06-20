package com.unitx.signal_core.theme

import android.graphics.Color
import androidx.core.graphics.toColorInt

internal object ThemeStaticDefaults {
    val light = ColorScheme(
        snackBackground = Color.WHITE,
        snackTextColor = Color.BLACK,
        snackActionTextColor = Color.BLACK,
        snackCancelIconTint = Color.BLACK,
        dialogBackground = Color.WHITE,
        dialogPrimaryButtonTextColor = Color.WHITE,
        loadingBackgroundGradient= Triple("#E8F3FA".toColorInt(), "#F0F7FF".toColorInt(), "#F8FBFF".toColorInt()),
        loadingTextColor= Color.BLACK,
        loadingAnimationActiveColor = Color.BLACK,
        loadingAnimationInactiveColor = Color.GRAY,
        loadingIconColor = Color.BLACK,
        loadingSimpleAnimationActiveColor = Color.WHITE,
        loadingSimpleAnimationInactiveColor = "#757575".toColorInt(),
    )

    val dark = ColorScheme(
        snackBackground = "#1E1E1E".toColorInt(),
        snackTextColor = Color.WHITE,
        snackActionTextColor = Color.WHITE,
        snackCancelIconTint = Color.WHITE,
        dialogBackground = Color.WHITE,
        dialogPrimaryButtonTextColor = Color.WHITE,
        loadingBackgroundGradient = Triple("#1E1E1E".toColorInt(), "#2C2C2C".toColorInt(), "#1E1E1E".toColorInt()),
        loadingTextColor = Color.WHITE,
        loadingAnimationActiveColor = Color.WHITE,
        loadingAnimationInactiveColor = "#5A5A5A".toColorInt(),
        loadingIconColor = Color.WHITE,
        loadingSimpleAnimationActiveColor = Color.WHITE,
        loadingSimpleAnimationInactiveColor = "#757575".toColorInt(),
    )

    fun resolve(scheme: ColorScheme, isNight: Boolean): ColorScheme {
        val defaults = if (isNight) dark else light
        return ColorScheme(
            snackBackground = scheme.snackBackground ?: defaults.snackBackground,
            snackTextColor = scheme.snackTextColor ?: defaults.snackTextColor,
            snackActionTextColor = scheme.snackActionTextColor ?: defaults.snackActionTextColor,
            snackCancelIconTint = scheme.snackCancelIconTint ?: defaults.snackCancelIconTint,

            dialogBackground = scheme.dialogBackground ?: defaults.dialogBackground,
            dialogPrimaryButtonTextColor = scheme.dialogPrimaryButtonTextColor ?: defaults.dialogPrimaryButtonTextColor,

            loadingBackgroundGradient = scheme.loadingBackgroundGradient ?: defaults.loadingBackgroundGradient,
            loadingTextColor = scheme.loadingTextColor ?: defaults.loadingTextColor,
            loadingAnimationActiveColor = scheme.loadingAnimationActiveColor ?: defaults.loadingAnimationActiveColor,
            loadingAnimationInactiveColor = scheme.loadingAnimationInactiveColor ?: defaults.loadingAnimationInactiveColor,
            loadingIconColor = scheme.loadingIconColor ?: defaults.loadingIconColor,
            loadingSimpleAnimationActiveColor = scheme.loadingSimpleAnimationActiveColor ?: defaults.loadingSimpleAnimationActiveColor,
            loadingSimpleAnimationInactiveColor = scheme.loadingSimpleAnimationInactiveColor ?: defaults.loadingSimpleAnimationInactiveColor,

            // Needs to be null if not defined in the global theme config as they are provided by config
            snackIconBackground = scheme.snackIconBackground,
            toastBackground = scheme.toastBackground,
            toastTextColor = scheme.toastTextColor,
            toastStrokeColor = scheme.toastStrokeColor,
            toastIconColor = scheme.toastIconColor,
            dialogPrimaryColor = scheme.dialogPrimaryColor,
            dialogSecondaryColor = scheme.dialogSecondaryColor,
            dialogTextColor = scheme.dialogTextColor,

        )
    }
}