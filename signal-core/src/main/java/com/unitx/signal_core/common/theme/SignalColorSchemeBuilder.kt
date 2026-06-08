package com.unitx.signal_core.common.theme

import androidx.annotation.ColorInt

class SignalColorSchemeBuilder {
    @ColorInt var snackBackground: Int? = null
    @ColorInt var snackTextColor: Int? = null
    @ColorInt var snackActionTextColor: Int? = null
    @ColorInt var snackCancelIconTint: Int? = null
//    @_root_ide_package_.androidx.annotation._root_ide_package_.androidx.annotation.ColorInt var snackIconBackground: Int? = null

    @ColorInt var toastBackground: Int? = null
    @ColorInt var toastTextColor: Int? = null
    @ColorInt var toastStrokeColor: Int? = null
    @ColorInt var toastIconColor: Int? = null

    @ColorInt var dialogBackground: Int? = null
    @ColorInt var dialogTextColor: Int? = null

    @ColorInt var bannerBackground: Int? = null
    @ColorInt var bannerTextColor: Int? = null

    fun build() = SignalColorScheme(
        snackBackground = snackBackground,
        snackTextColor = snackTextColor,
        snackActionTextColor = snackActionTextColor,
        snackCancelIconTint = snackCancelIconTint,
//        snackIconBackground = snackIconBackground,
        toastBackground = toastBackground,
        toastTextColor = toastTextColor,
        dialogBackground = dialogBackground,
        dialogTextColor = dialogTextColor,
        bannerBackground = bannerBackground,
        bannerTextColor = bannerTextColor,
        toastStrokeColor = toastStrokeColor,
        toastIconColor = toastIconColor,
    )
}