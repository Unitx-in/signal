package com.unitx.signal_core.contract.type

sealed class LoadingAnimation {
    object Default : LoadingAnimation()
    data class Lottie(val assetPath: String) : LoadingAnimation()
    data class LottieRes(val rawRes: Int) : LoadingAnimation()
    data class Drawable(val drawableRes: Int) : LoadingAnimation()
}