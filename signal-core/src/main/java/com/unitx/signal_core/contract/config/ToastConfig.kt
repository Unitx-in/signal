package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.ToastType

class ToastConfig {
    var tag: String? = null
    var duration: Long = 2000L
    var position: ToastPosition = ToastPosition.Bottom
    var type: ToastType = ToastType.Info
    var dismissOnTap: Boolean = true
    var topOffset: Int = 0
    var bottomOffset: Int = 0
    @DrawableRes
    var iconRes: Int? = null
    var iconPosition: IconPosition = IconPosition.Start
    var onShown: (() -> Unit)? = null
    var onDismissed: (() -> Unit)? = null

    internal fun copy(): ToastConfig = ToastConfig().also {
        it.duration = duration
        it.position = position
        it.type = type
        it.iconRes = iconRes
        it.iconPosition = iconPosition
        it.dismissOnTap = dismissOnTap
        it.tag = tag
        it.topOffset = topOffset
        it.bottomOffset = bottomOffset
        it.onShown = onShown
        it.onDismissed = onDismissed
    }
}