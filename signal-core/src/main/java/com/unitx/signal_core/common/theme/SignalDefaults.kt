package com.unitx.signal_core.common.theme

import android.graphics.Color
import androidx.core.graphics.toColorInt

object SignalDefaults {
    val light = SignalColorScheme(
        snackBackground = Color.WHITE,
        snackTextColor = Color.BLACK,
        snackActionTextColor = Color.BLACK,
        snackCancelIconTint = Color.BLACK,
//        snackIconBackground = Color.LTGRAY,

        toastBackground = Color.WHITE,
        toastTextColor = Color.BLACK,

        dialogBackground = Color.WHITE,
        dialogTextColor = Color.BLACK,

        bannerBackground = Color.WHITE,
        bannerTextColor = Color.BLACK,
    )

    val dark = SignalColorScheme(
        snackBackground = "#1E1E1E".toColorInt(),
        snackTextColor = Color.WHITE,
        snackActionTextColor = Color.WHITE,
        snackCancelIconTint = Color.WHITE,
//        snackIconBackground = Color.DKGRAY,

        toastBackground = "#1E1E1E".toColorInt(),
        toastTextColor = Color.WHITE,

        dialogBackground = "#1E1E1E".toColorInt(),
        dialogTextColor = Color.WHITE,

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
//            snackIconBackground = scheme.snackIconBackground, // Needs to be null if not defined in global config, as colors will be provided by Snack type

            toastBackground = scheme.toastBackground ?: defaults.toastBackground,
            toastTextColor = scheme.toastTextColor ?: defaults.toastTextColor,

            dialogBackground = scheme.dialogBackground ?: defaults.dialogBackground,
            dialogTextColor = scheme.dialogTextColor ?: defaults.dialogTextColor,

            bannerBackground = scheme.bannerBackground ?: defaults.bannerBackground,
            bannerTextColor = scheme.bannerTextColor ?: defaults.bannerTextColor,
        )
    }
}