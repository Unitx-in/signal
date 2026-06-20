package com.unitx.signal_core.contract.model

/**
 * Fine-grained control over the loading dots animation.
 *
 * Defaults produce a smooth, staggered pulse. Only override if you need
 * custom timing or scale behavior.
 */
data class LoadingAnimationAttr(

    /** Duration of each dot's scale/alpha animation in ms. Default: 700. */
    val dotAnimationDuration: Long = 700L,

    /** Stagger delay between each dot's animation start in ms. Default: 350. */
    val dotStartDelay: Long = 350L,

    /** Pause before the animation loop repeats in ms. Default: 2400. */
    val animationLoopDelay: Long = 2400L,

    /** Scale of the active (bouncing) dot. Default: 1.4. */
    val activeScale: Float = 1.4f,

    /** Scale of the inactive (resting) dot. Default: 1.0. */
    val inactiveScale: Float = 1f,

    /** Alpha of the active dot. Default: 1.0. */
    val activeAlpha: Float = 1f,

    /** Alpha of the inactive dot. Default: 0.4. */
    val inactiveAlpha: Float = 0.4f
)