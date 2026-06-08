package com.unitx.signal_core.common.config.base

import com.unitx.signal_core.common.type.SnackPosition
import com.unitx.signal_core.common.type.SnackType

class SnackConfig {
    var message: String = ""
    var duration: Long = 2500L
    var dismissOnBackPress: Boolean = false
    var anchorViewId: Int? = null
    var position: SnackPosition = SnackPosition.Bottom
    var type: SnackType = SnackType.Info
    var showCancelAction: Boolean = true

    internal var action: Pair<String, () -> Unit>? = null
    fun action(label: String, block: () -> Unit) { action = label to block }

    internal fun copy(): SnackConfig = SnackConfig().also {
        it.message = message
        it.duration = duration
        it.dismissOnBackPress = dismissOnBackPress
        it.anchorViewId = anchorViewId
        it.position = position
        it.type = type
        it.showCancelAction = showCancelAction
        it.action = action
    }
}