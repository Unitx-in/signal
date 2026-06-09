package com.unitx.signal_core.contract.config.base

import com.unitx.signal_core.contract.position.BannerPosition
import com.unitx.signal_core.contract.type.SignalType

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