package com.unitx.signal_core.contract.model

data class LoadingAnimationAttr(

    val dotAnimationDuration: Long = 700L,
    val dotStartDelay: Long = 350L,
    val animationLoopDelay: Long = 2400L,

    val activeScale: Float = 1.4f,
    val inactiveScale: Float = 1f,

    val activeAlpha: Float = 1f,
    val inactiveAlpha: Float = 0.4f
)