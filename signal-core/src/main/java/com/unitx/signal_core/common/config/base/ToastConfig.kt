package com.unitx.signal_core.common.config.base

import com.unitx.signal_core.common.type.SignalDuration
import com.unitx.signal_core.common.type.SignalType

class ToastConfig {
    var duration: SignalDuration = SignalDuration.Short
    var type: SignalType = SignalType.Default
}