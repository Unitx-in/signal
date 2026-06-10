package com.unitx.signal_core.contract.config

import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType

class SnackConfig {
    var tag: String? = null
    var message: String = ""
    var duration: Long = 2500L
    var dismissOnBackPress: Boolean = false
    var topOffset: Int = 0
    var bottomOffset: Int = 0
    var position: SnackPosition = SnackPosition.Bottom
    var type: SnackType = SnackType.Info
    var showCancelAction: Boolean = true

    var onShown: (() -> Unit)? = null
    var onDismissed: (() -> Unit)? = null

    internal var action: Pair<String, () -> Unit>? = null
    fun action(label: String, block: () -> Unit) { action = label to block }

    internal fun copy(): SnackConfig = SnackConfig().also {
        it.message = message
        it.duration = duration
        it.dismissOnBackPress = dismissOnBackPress
        it.topOffset = topOffset
        it.bottomOffset = bottomOffset
        it.position = position
        it.type = type
        it.showCancelAction = showCancelAction
        it.action = action
        it.tag = tag
        it.onShown = onShown
        it.onDismissed = onDismissed
    }
}