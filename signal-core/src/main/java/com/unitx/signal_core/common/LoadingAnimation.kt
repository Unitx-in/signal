package com.unitx.signal_core.common

sealed class LoadingAnimation {
    object Default : LoadingAnimation()
    data class Lottie(val assetPath: String) : LoadingAnimation()
    data class LottieRes(val rawRes: Int) : LoadingAnimation()
    data class Drawable(val drawableRes: Int) : LoadingAnimation()
}