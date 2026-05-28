package com.unitx.signal_core.handler.toast

import android.view.View

class ToastAnimator {

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
}