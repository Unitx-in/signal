package com.unitx.signal_core.common.config.base

import androidx.annotation.DrawableRes
import com.unitx.signal_core.common.position.IconPosition
import com.unitx.signal_core.common.position.ToastPosition
import com.unitx.signal_core.common.type.ToastType

class ToastConfig {
    var duration: Long = 2000L
    var position: ToastPosition = ToastPosition.Bottom
    var type: ToastType = ToastType.Info
    @DrawableRes
    var iconRes: Int? = null
    var iconPosition: IconPosition = IconPosition.Start

    internal fun copy(): ToastConfig = ToastConfig().also {
        it.duration = duration
        it.position = position
        it.type = type
        it.iconRes = iconRes
        it.iconPosition = iconPosition
    }
}