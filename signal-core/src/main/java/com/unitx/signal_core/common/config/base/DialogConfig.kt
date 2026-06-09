package com.unitx.signal_core.common.config.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unitx.signal_core.common.type.DialogType

class DialogConfig {
    var title: String = ""
    var message: String = ""
    var header: String = ""
    @DrawableRes var icon: Int? = null
    var type: DialogType = DialogType.Default
    var cancelable: Boolean = true
    var horizontalMargin: Int = 24
    internal var positive: Pair<String, () -> Unit>? = null
    internal var negative: Pair<String, () -> Unit>? = null
    var autoDismiss: Boolean = false
    var duration: Long = 4000L

    fun positive(label: String, block: () -> Unit) { positive = label to block }
    fun negative(label: String, block: () -> Unit = {}) { negative = label to block }

    internal fun copy(): DialogConfig = DialogConfig().also {
        it.title = title
        it.message = message
        it.type = type
        it.cancelable = cancelable
        it.autoDismiss = autoDismiss
        it.duration = duration
        it.icon = icon
        it.header = header
        it.horizontalMargin = horizontalMargin
        it.positive = positive
        it.negative = negative
    }
}