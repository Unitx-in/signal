package com.unitx.signal_core.theme

import androidx.annotation.ColorInt

class SignalColorSchemeBuilder {
    @ColorInt var snackBackground: Int? = null
    @ColorInt var snackTextColor: Int? = null
    @ColorInt var snackActionTextColor: Int? = null
    @ColorInt var snackCancelIconTint: Int? = null

    @ColorInt var toastBackground: Int? = null
    @ColorInt var toastTextColor: Int? = null
    @ColorInt var toastStrokeColor: Int? = null
    @ColorInt var toastIconColor: Int? = null

    @ColorInt val dialogBackground: Int? = null
    @ColorInt val dialogPrimaryColor: Int? = null
    @ColorInt val dialogSecondaryColor: Int? = null
    @ColorInt val dialogTextColor: Int? = null
    @ColorInt val dialogPrimaryButtonTextColor : Int? = null

    @ColorInt var bannerBackground: Int? = null
    @ColorInt var bannerTextColor: Int? = null

    fun build() = SignalColorScheme(
        snackBackground = snackBackground,
        snackTextColor = snackTextColor,
        snackActionTextColor = snackActionTextColor,
        snackCancelIconTint = snackCancelIconTint,
        toastBackground = toastBackground,
        toastTextColor = toastTextColor,
        dialogBackground = dialogBackground,
        dialogPrimaryColor = dialogPrimaryColor,
        dialogSecondaryColor = dialogSecondaryColor,
        dialogTextColor = dialogTextColor,
        dialogPrimaryButtonTextColor = dialogPrimaryButtonTextColor,
        bannerBackground = bannerBackground,
        bannerTextColor = bannerTextColor,
        toastStrokeColor = toastStrokeColor,
        toastIconColor = toastIconColor,
    )
}