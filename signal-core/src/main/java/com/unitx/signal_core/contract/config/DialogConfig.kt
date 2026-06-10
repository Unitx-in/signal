package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.model.SignalAction
import com.unitx.signal_core.contract.type.DialogType

class DialogConfig {

    internal var positive: SignalAction? = null
    internal var negative: SignalAction? = null
    internal var neutral: SignalAction? = null

    var title: String = ""
    var message: String = ""
    var header: String = ""
    @DrawableRes
    var icon: Int? = null
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
    var accessibilityText: String? = null

    fun positive(label: String, onClick: () -> Unit) {
        positive = SignalAction(
            label = label,
            onClick = onClick
        )
    }

    fun negative(label: String, onClick: () -> Unit = {}) {
        negative = SignalAction(
            label = label,
            onClick = onClick
        )
    }

    fun neutral(label: String, onClick: () -> Unit = {}) {
        neutral = SignalAction(
            label = label,
            onClick = onClick
        )
    }

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
        it.accessibilityText = accessibilityText
    }
}