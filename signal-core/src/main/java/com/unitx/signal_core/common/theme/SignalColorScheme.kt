package com.unitx.signal_core.common.theme

import androidx.annotation.ColorInt

data class SignalColorScheme(
    @ColorInt val snackBackground: Int? = null,
    @ColorInt val snackTextColor: Int? = null,
    @ColorInt val snackActionTextColor: Int? = null,
    @ColorInt val snackCancelIconTint: Int? = null,
//    @ColorInt val snackIconBackground: Int? = null,

    @ColorInt val toastBackground: Int? = null,
    @ColorInt val toastTextColor: Int? = null,

    @ColorInt val dialogBackground: Int? = null,
    @ColorInt val dialogTextColor: Int? = null,

    @ColorInt val bannerBackground: Int? = null,
    @ColorInt val bannerTextColor: Int? = null,
)