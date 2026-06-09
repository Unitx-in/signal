package com.unitx.signal_core.contract.config.base

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.ToastType

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