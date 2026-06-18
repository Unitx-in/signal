package com.unitx.signal_core.helper

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.drawable.DrawableCompat
import com.unitx.signal_core.contract.model.LoadingAnimationAttr

internal class LoadingDotsAnimator(
    private val dots: List<View>, private val config: LoadingAnimationAttr
) {

    private var activeColor: Int? = null
    private var inactiveColor: Int? = null

    private var running = false

    private val animationRunnable = object : Runnable {
        override fun run() {

            if (!running) return

            dots.forEachIndexed { index, dot ->
                animateDot(
                    dot = dot, delay = index * config.dotStartDelay
                )
            }

            dots.firstOrNull()?.postDelayed(
                this, config.animationLoopDelay
            )
        }
    }

    fun start() {
        if (running) return

        running = true
        dots.firstOrNull()?.post(animationRunnable)
    }

    fun stop() {
        running = false
        dots.firstOrNull()?.removeCallbacks(animationRunnable)

        dots.forEach {
            it.animate().cancel()
            it.scaleX = config.inactiveScale
            it.scaleY = config.inactiveScale
            it.alpha = config.inactiveAlpha
            tint(it.background, inactiveColor)
        }
    }

    fun updateColors(activeColor: Int, inactiveColor: Int) {
        this.activeColor = activeColor
        this.inactiveColor = inactiveColor

        if (!running) {
            dots.forEach { tint(it.background, inactiveColor) }
        }
    }

    private fun animateDot(
        dot: View, delay: Long
    ) {

        val scaleX = ObjectAnimator.ofFloat(
            dot, View.SCALE_X, config.inactiveScale, config.activeScale, config.inactiveScale
        )

        val scaleY = ObjectAnimator.ofFloat(
            dot, View.SCALE_Y, config.inactiveScale, config.activeScale, config.inactiveScale
        )

        val alpha = ObjectAnimator.ofFloat(
            dot, View.ALPHA, config.inactiveAlpha, config.activeAlpha, config.inactiveAlpha
        )

        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = config.dotAnimationDuration
            startDelay = delay

            addUpdateListener { animator ->
                val color = if (animator.animatedFraction < 0.5f) activeColor
                else inactiveColor

                tint(dot.background, color)
            }

            start()
        }

        AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = config.dotAnimationDuration
            startDelay = delay
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun tint(
        drawable: Drawable?, color: Int?
    ) {
        if (drawable == null || color == null) return
        DrawableCompat.setTint(drawable.mutate(), color)
    }
}