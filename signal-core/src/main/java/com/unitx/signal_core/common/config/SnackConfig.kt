package com.unitx.signal_core.common.config

import com.unitx.signal_core.common.SnackPosition
import com.unitx.signal_core.common.SnackType

class SnackConfig {
    var message: String = ""
    var duration: Long = 2500L
    var dismissOnBackPress: Boolean = false
    var anchorViewId: Int? = null
    var position: SnackPosition = SnackPosition.Bottom

    // call-site only — no default
    var type: SnackType? = null

    internal var action: Pair<String, () -> Unit>? = null
    fun action(label: String, block: () -> Unit) { action = label to block }
}