package com.unitx.signal_core.theme

import androidx.annotation.ColorInt

data class SignalColorScheme(
    @ColorInt val snackBackground: Int? = null,
    @ColorInt val snackTextColor: Int? = null,
    @ColorInt val snackActionTextColor: Int? = null,
    @ColorInt val snackCancelIconTint: Int? = null,
    @ColorInt val snackIconBackground: Int? = null,


    @ColorInt val toastBackground: Int? = null,
    @ColorInt val toastStrokeColor: Int? = null,
    @ColorInt val toastTextColor: Int? = null,
    @ColorInt val toastIconColor: Int? = null,


    @ColorInt val dialogBackground: Int? = null,
    @ColorInt val dialogPrimaryColor: Int? = null,
    @ColorInt val dialogSecondaryColor: Int? = null,
    @ColorInt val dialogTextColor: Int? = null,
    @ColorInt val dialogPrimaryButtonTextColor : Int? = null,

    @ColorInt val loadingBackgroundGradient: Triple<Int, Int, Int>? = null,
    @ColorInt val loadingTextColor: Int? = null,
    @ColorInt val loadingAnimationActiveColor : Int? = null,
    @ColorInt val loadingAnimationInactiveColor : Int? = null,
    @ColorInt val loadingIconColor : Int? = null,

    @ColorInt val bannerBackground: Int? = null,
    @ColorInt val bannerTextColor: Int? = null,
)