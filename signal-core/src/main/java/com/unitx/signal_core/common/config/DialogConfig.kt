package com.unitx.signal_core.common.config

import com.unitx.signal_core.common.SignalType

class DialogConfig {
    var title: String = ""
    var message: String = ""
    var type: SignalType = SignalType.Default
    var cancelable: Boolean = true
    internal var positive: Pair<String, () -> Unit>? = null
    internal var negative: Pair<String, () -> Unit>? = null
    internal var neutral: Pair<String, () -> Unit>? = null

    fun positive(label: String, block: () -> Unit) { positive = label to block }
    fun negative(label: String, block: () -> Unit = {}) { negative = label to block }
    fun neutral(label: String, block: () -> Unit = {}) { neutral = label to block }
}