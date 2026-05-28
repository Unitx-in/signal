package com.unitx.signal_core.common.theme

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

class SignalColorSchemeBuilder {
    @ColorRes var snackBackground: Int? = null
    @ColorRes var snackTextColor: Int? = null
    @ColorRes var snackActionTextColor: Int? = null
    @ColorRes var snackCancelIconTint: Int? = null
//    @_root_ide_package_.androidx.annotation.ColorRes var snackIconBackground: Int? = null

    @ColorRes var toastBackground: Int? = null
    @ColorRes var toastTextColor: Int? = null

    @ColorRes var dialogBackground: Int? = null
    @ColorRes var dialogTextColor: Int? = null

    @ColorRes var bannerBackground: Int? = null
    @ColorRes var bannerTextColor: Int? = null

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
    )
}