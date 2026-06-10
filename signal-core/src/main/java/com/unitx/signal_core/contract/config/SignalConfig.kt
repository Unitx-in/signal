package com.unitx.signal_core.contract.config

import com.unitx.signal_core.contract.type.QueueType
import com.unitx.signal_core.theme.SignalTheme

class SignalConfig {
    internal val snackConfig = SnackConfig()
    internal val toastConfig = ToastConfig()
    internal val dialogConfig = DialogConfig()
    internal val theme = SignalTheme()
    internal var queueType: QueueType = QueueType.default

    fun snack(block: SnackConfig.() -> Unit) { snackConfig.apply(block) }
    fun toast(block: ToastConfig.() -> Unit) { toastConfig.apply(block) }
    fun dialog(block: DialogConfig.() -> Unit) { dialogConfig.apply(block) }
    fun theme(block: SignalTheme.() -> Unit) { theme.apply(block) }
    fun setQueueType(type: QueueType) { this.queueType = type }
}