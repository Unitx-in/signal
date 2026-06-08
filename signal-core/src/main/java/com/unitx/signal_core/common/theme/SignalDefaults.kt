package com.unitx.signal_core.common.theme

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.unitx.signal_core.common.type.SnackType

object SignalDefaults {
    val light = SignalColorScheme(
        snackBackground = Color.WHITE,
        snackTextColor = Color.BLACK,
        snackActionTextColor = Color.BLACK,
        snackCancelIconTint = Color.BLACK,
        dialogBackground = Color.WHITE,
        dialogTextColor = Color.BLACK,
        bannerBackground = Color.WHITE,
        bannerTextColor = Color.BLACK,

//        These defaults are defined in Types
//        snackIconBackground: Color.LTGRAY,
//        toastBackground = Color.WHITE,
//        toastTextColor = Color.BLACK,
//        toastStrokeColor = Color.BLACK,
//        toastIconColor = Color.BLACK,
    )

    val dark = SignalColorScheme(
        snackBackground = "#1E1E1E".toColorInt(),
        snackTextColor = Color.WHITE,
        snackActionTextColor = Color.WHITE,
        snackCancelIconTint = Color.WHITE,
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
            dialogBackground = scheme.dialogBackground ?: defaults.dialogBackground,
            dialogTextColor = scheme.dialogTextColor ?: defaults.dialogTextColor,
            bannerBackground = scheme.bannerBackground ?: defaults.bannerBackground,
            bannerTextColor = scheme.bannerTextColor ?: defaults.bannerTextColor,

            // Needs to be null if not defined in the global theme cofig
            snackIconBackground = scheme.snackIconBackground,
            toastBackground = scheme.toastBackground,
            toastTextColor = scheme.toastTextColor,
            toastStrokeColor = scheme.toastStrokeColor,
            toastIconColor = scheme.toastIconColor,
        )
    }
}