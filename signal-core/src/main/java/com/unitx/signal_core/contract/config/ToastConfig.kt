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
    @DrawableRes
    var iconRes: Int? = null
    var iconPosition: IconPosition = IconPosition.Start

    internal fun copy(): ToastConfig = ToastConfig().also {
        it.duration = duration
        it.position = position
        it.type = type
        it.iconRes = iconRes
        it.iconPosition = iconPosition
        it.dismissOnTap = dismissOnTap
        it.tag = tag
    }
}