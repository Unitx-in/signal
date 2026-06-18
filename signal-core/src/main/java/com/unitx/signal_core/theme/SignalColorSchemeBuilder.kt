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

    @ColorInt var dialogBackground: Int? = null
    @ColorInt var dialogPrimaryColor: Int? = null
    @ColorInt var dialogSecondaryColor: Int? = null
    @ColorInt var dialogTextColor: Int? = null
    @ColorInt var dialogPrimaryButtonTextColor : Int? = null
    @ColorInt var loadingBackground: Triple<Int, Int, Int>? = null
    @ColorInt var loadingTextColor: Int? = null
    @ColorInt var loadingAnimationActiveColor : Int? = null
    @ColorInt var loadingAnimationInactiveColor : Int? = null
    @ColorInt var loadingIconColor : Int? = null
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
        loadingBackgroundGradient = loadingBackground,
        loadingTextColor = loadingTextColor,
        loadingAnimationActiveColor = loadingAnimationActiveColor,
        loadingAnimationInactiveColor = loadingAnimationInactiveColor,
        loadingIconColor = loadingIconColor,
    )
}