package com.unitx.signal_core.theme

import android.graphics.Color
import androidx.core.graphics.toColorInt

internal object SignalStaticDefaults {
    val light = SignalColorScheme(
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

        bannerBackground = Color.WHITE,
        bannerTextColor = Color.BLACK,
    )

    val dark = SignalColorScheme(
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

        bannerBackground = "#1E1E1E".toColorInt(),
        bannerTextColor = Color.WHITE,
    )

    fun resolve(scheme: SignalColorScheme, isNight: Boolean): SignalColorScheme {
        val defaults = if (isNight) dark else light
        return SignalColorScheme(
            snackBackground = scheme.snackBackground ?: defaults.snackBackground,
            snackTextColor = scheme.snackTextColor ?: defaults.snackTextColor,
            snackActionTextColor = scheme.snackActionTextColor ?: defaults.snackActionTextColor,
            snackCancelIconTint = scheme.snackCancelIconTint ?: defaults.snackCancelIconTint,

            dialogBackground = scheme.dialogBackground ?: defaults.dialogBackground,
            dialogPrimaryButtonTextColor = scheme.dialogPrimaryButtonTextColor ?: defaults.dialogPrimaryButtonTextColor,

            bannerBackground = scheme.bannerBackground ?: defaults.bannerBackground,
            bannerTextColor = scheme.bannerTextColor ?: defaults.bannerTextColor,

            loadingBackgroundGradient = scheme.loadingBackgroundGradient ?: defaults.loadingBackgroundGradient,
            loadingTextColor = scheme.loadingTextColor ?: defaults.loadingTextColor,
            loadingAnimationActiveColor = scheme.loadingAnimationActiveColor ?: defaults.loadingAnimationActiveColor,
            loadingAnimationInactiveColor = scheme.loadingAnimationInactiveColor ?: defaults.loadingAnimationInactiveColor,
            loadingIconColor = scheme.loadingIconColor ?: defaults.loadingIconColor,


            // Needs to be null if not defined in the global theme cofig
            snackIconBackground = scheme.snackIconBackground,
            toastBackground = scheme.toastBackground,
            toastTextColor = scheme.toastTextColor,
            toastStrokeColor = scheme.toastStrokeColor,
            toastIconColor = scheme.toastIconColor,
            dialogPrimaryColor = scheme.dialogPrimaryColor,
            dialogSecondaryColor = scheme.dialogSecondaryColor,
            dialogTextColor = scheme.dialogTextColor
        )
    }
}