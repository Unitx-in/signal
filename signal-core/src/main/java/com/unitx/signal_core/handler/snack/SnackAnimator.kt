package com.unitx.signal_core.handler.snack

import android.view.View
import androidx.core.view.doOnLayout
import com.unitx.signal_core.common.SnackPosition

class SnackAnimator {

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