package com.unitx.signal_core.common.helper

import android.view.View
import androidx.core.view.doOnLayout
import com.unitx.signal_core.common.type.SnackPosition

class SignalAnimator {

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
                onEnd()
            }
            .start()
    }
    fun slideIn(container: View, position: SnackPosition) {
        container.visibility = View.VISIBLE
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
}