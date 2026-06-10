package com.unitx.signal_core.contract.config

import com.unitx.signal_core.queue.QueueStrategy
import com.unitx.signal_core.theme.SignalTheme

class SignalConfig {
    internal val snackConfig = SnackConfig()
    internal val toastConfig = ToastConfig()
    internal val dialogConfig = DialogConfig()
    internal val theme = SignalTheme()
    internal var queueStrategy: QueueStrategy = QueueStrategy.default

    fun snack(block: SnackConfig.() -> Unit) { snackConfig.apply(block) }
    fun toast(block: ToastConfig.() -> Unit) { toastConfig.apply(block) }
    fun dialog(block: DialogConfig.() -> Unit) { dialogConfig.apply(block) }
    fun theme(block: SignalTheme.() -> Unit) { theme.apply(block) }
    fun setQueueStrategy(strategy: QueueStrategy) { queueStrategy = strategy }
}