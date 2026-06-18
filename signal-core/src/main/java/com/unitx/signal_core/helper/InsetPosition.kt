package com.unitx.signal_core.helper

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unitx.signal_core.contract.position.EdgePosition

internal fun applyInsetPosition(
    root: View,
    position: EdgePosition,
    topOffset: Int = 0,
    bottomOffset: Int = 0,
    centerHorizontal: Boolean = false
) {
    val layoutParams = root.layoutParams as FrameLayout.LayoutParams
    layoutParams.gravity = when (position) {
        EdgePosition.Bottom -> Gravity.BOTTOM
        EdgePosition.Top -> Gravity.TOP
        EdgePosition.Center -> Gravity.CENTER_VERTICAL
    }.let { if (centerHorizontal) it or Gravity.CENTER_HORIZONTAL else it }
    root.layoutParams = layoutParams

    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.topMargin = 0
        params.bottomMargin = 0
        when (position) {
            EdgePosition.Bottom -> params.bottomMargin = systemBars.bottom + bottomOffset
            EdgePosition.Top -> params.topMargin = systemBars.top + topOffset
            EdgePosition.Center -> {}
        }
        view.layoutParams = params
        insets
    }
    ViewCompat.requestApplyInsets(root)
}