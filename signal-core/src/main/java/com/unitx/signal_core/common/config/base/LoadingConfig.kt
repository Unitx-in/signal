package com.unitx.signal_core.common.config.base

import com.unitx.signal_core.common.type.LoadingAnimation

class LoadingConfig {
    var message: String = "Please wait..."
    var cancelable: Boolean = false
    var animation: LoadingAnimation = LoadingAnimation.Default
}