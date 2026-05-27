package com.unitx.signal_core.common.config

import com.unitx.signal_core.common.BannerPosition
import com.unitx.signal_core.common.SignalType

class BannerConfig {
    var type: SignalType = SignalType.Info
    var autoDismiss: Boolean = true
    var autoDismissDelay: Long = 3000L
    var dismissOnBackPress: Boolean = true
    var position: BannerPosition = BannerPosition.Top
    internal var action: Pair<String, () -> Unit>? = null

    fun action(label: String, block: () -> Unit) {
        action = label to block
    }
}