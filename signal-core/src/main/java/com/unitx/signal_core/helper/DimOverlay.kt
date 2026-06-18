package com.unitx.signal_core.helper

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

internal class DimOverlay {

    private var overlay: View? = null

    val isAttached: Boolean
        get() = overlay != null

    fun attach(
        context: Context,
        rootView: ViewGroup,
        cancelable: Boolean,
        onDismiss: () -> Unit
    ) {
        if (overlay != null) return
        overlay = View(context).apply {
            setBackgroundColor(0x99000000.toInt())
            alpha = 0f
            isClickable = true
            isFocusable = true
            animate().alpha(1f).setDuration(220).start()
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            if (cancelable) setOnClickListener { onDismiss() }
        }
        rootView.addView(overlay)
    }

    fun release(rootView: ViewGroup?, onReleased: () -> Unit = {}) {
        overlay?.animate()?.alpha(0f)?.setDuration(180)?.withEndAction {
            rootView?.removeView(overlay)
            overlay = null
            onReleased()
        }?.start() ?: onReleased()
    }
}