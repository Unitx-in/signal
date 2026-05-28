package com.unitx.signal_core.common.theme

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

data class SignalColorScheme(
    @ColorRes val snackBackground: Int? = null,
    @ColorRes val snackTextColor: Int? = null,
    @ColorRes val snackActionTextColor: Int? = null,
    @ColorRes val snackCancelIconTint: Int? = null,

    @ColorRes val toastBackground: Int? = null,
    @ColorRes val toastStrokeColor: Int? = null,
    @ColorRes val toastTextColor: Int? = null,
    @ColorRes val toastIconColor: Int? = null,


    @ColorRes val dialogBackground: Int? = null,
    @ColorRes val dialogTextColor: Int? = null,

    @ColorRes val bannerBackground: Int? = null,
    @ColorRes val bannerTextColor: Int? = null,
)