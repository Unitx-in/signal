package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.type.DialogType

class DialogConfig {

    internal var positive: Pair<String, () -> Unit>? = null
    internal var negative: Pair<String, () -> Unit>? = null
    internal var neutral: Pair<String, () -> Unit>? = null

    var title: String = ""
    var message: String = ""
    var header: String = ""
    @DrawableRes var icon: Int? = null
    var type: DialogType = DialogType.Default
    var cancelable: Boolean = false
    var horizontalMargin: Int = 24
    var autoDismiss: Boolean = false
    var autoDismissDuration: Long = 4000L
    var dismissOnPositive: Boolean = true
    var dismissOnNegative: Boolean = true
    var dismissOnNeutral: Boolean = true

    var onShown: (() -> Unit)? = null
    var onDismissed: (() -> Unit)? = null

    fun positive(label: String, block: () -> Unit) { positive = label to block }
    fun negative(label: String, block: () -> Unit = {}) { negative = label to block }
    fun neutral(label: String, block: () -> Unit = {}) { neutral = label to block }

    internal fun copy(): DialogConfig = DialogConfig().also {
        it.title = title
        it.message = message
        it.type = type
        it.cancelable = cancelable
        it.autoDismiss = autoDismiss
        it.autoDismissDuration = autoDismissDuration
        it.icon = icon
        it.header = header
        it.horizontalMargin = horizontalMargin
        it.positive = positive
        it.negative = negative
        it.neutral = neutral
        it.dismissOnPositive = dismissOnPositive
        it.dismissOnNegative = dismissOnNegative
        it.onShown = onShown
        it.onDismissed = onDismissed
    }
}