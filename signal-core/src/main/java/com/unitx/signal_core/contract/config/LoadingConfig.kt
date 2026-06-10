package com.unitx.signal_core.contract.config

import com.unitx.signal_core.contract.type.LoadingAnimation

class LoadingConfig {
    var message: String = "Please wait..."
    var cancelable: Boolean = false
    var animation: LoadingAnimation = LoadingAnimation.Default
}