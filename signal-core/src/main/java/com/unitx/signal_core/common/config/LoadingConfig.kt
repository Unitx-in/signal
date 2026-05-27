package com.unitx.signal_core.common.config

import com.unitx.signal_core.common.LoadingAnimation

class LoadingConfig {
    var message: String = "Please wait..."
    var cancelable: Boolean = false
    var animation: LoadingAnimation = LoadingAnimation.Default
}