package com.unitx.signal_core.common.config.base

import com.unitx.signal_core.common.type.IconPosition
import com.unitx.signal_core.common.type.ToastPosition
import com.unitx.signal_core.common.type.ToastType

class ToastConfig {
    var duration: Long = 2000L
    var position: ToastPosition = ToastPosition.Bottom
    var type: ToastType = ToastType.Info
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