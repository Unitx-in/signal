package com.unitx.signal_core.helper

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.view.doOnLayout
import com.unitx.signal_core.contract.position.NotificationPosition
import com.unitx.signal_core.contract.position.SnackPosition

internal object SignalAnimator {

    fun fadeIn(container: View) {
        container.visibility = View.VISIBLE
        container.alpha = 0f
        container.animate()
            .alpha(1f)
            .setDuration(250)
            .start()
    }

    fun fadeOut(container: View, onEnd: () -> Unit) {
        container.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                container.visibility = View.GONE
                container.alpha = 1f
                onEnd()
            }
            .start()
    }
    fun slideIn(container: View, position: SnackPosition) {
        container.visibility = View.VISIBLE
        container.alpha = 1f
        container.doOnLayout {
            val startY = when (position) {
                SnackPosition.Bottom -> container.height.toFloat()
                SnackPosition.Top -> -container.height.toFloat()
            }
            container.translationY = startY
            container.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

    fun slideOut(container: View, position: SnackPosition, onEnd: () -> Unit) {
        val endY = when (position) {
            SnackPosition.Bottom -> container.height.toFloat()
            SnackPosition.Top -> -container.height.toFloat()
        }
        container.animate()
            .translationY(endY)
            .setDuration(250)
            .withEndAction {
                container.visibility = View.GONE
                onEnd()
            }
            .start()
    }

    fun slideIn(container: View, position: NotificationPosition) {
        container.visibility = View.VISIBLE
        container.alpha = 1f
        if (position == NotificationPosition.Center) {
            fadeIn(container)
            return
        }
        container.doOnLayout {
            val startY = when (position) {
                NotificationPosition.Bottom -> container.height.toFloat()
                NotificationPosition.Top -> -container.height.toFloat()
                NotificationPosition.Center -> 0f
            }
            container.translationY = startY
            container.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

    fun slideOut(container: View, position: NotificationPosition, onEnd: () -> Unit) {
        if (position == NotificationPosition.Center) {
            fadeOut(container, onEnd)
            return
        }
        val endY = when (position) {
            NotificationPosition.Bottom -> container.height.toFloat()
            NotificationPosition.Top -> -container.height.toFloat()
            NotificationPosition.Center -> 0f
        }
        container.animate()
            .translationY(endY)
            .setDuration(250)
            .withEndAction {
                container.visibility = View.GONE
                onEnd()
            }
            .start()
    }

    fun scaleIn(container: View) {
        container.visibility = View.VISIBLE
        container.scaleX = 0.85f
        container.scaleY = 0.85f
        container.alpha = 0f
        container.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(220)
            .setInterpolator(OvershootInterpolator(1.2f))
            .start()
    }

    fun scaleOut(container: View, onEnd: () -> Unit) {
        container.animate()
            .scaleX(0.85f)
            .scaleY(0.85f)
            .alpha(0f)
            .setDuration(180)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                container.visibility = View.GONE
                onEnd()
            }
            .start()
    }
}