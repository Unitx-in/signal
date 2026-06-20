package com.unitx.signal_core.contract.config

import com.unitx.signal_core.queue.QueueStrategy
import com.unitx.signal_core.theme.ThemeConfig

/**
 * Global configuration for Signal. Set defaults for all signal types at initialization.
 *
 * Usage:
 * ```
 * SignalCore.init(app) {
 *     toast { duration = 3000L }
 *     snack { position = SnackPosition.Top }
 *     dialog { cancelable = true }
 *     loading { simpleLoading = true }
 *     theme { /* customize colors */ }
 *     setQueueStrategy(QueueStrategy.GlobalSequential)
 * }
 * ```
 */
class SignalConfig {
    internal val snackConfig = SnackConfig()
    internal val toastConfig = ToastConfig()
    internal val dialogConfig = DialogConfig()
    internal val loadingConfig = LoadingConfig()

    internal val theme = ThemeConfig()
    internal var queueStrategy: QueueStrategy = QueueStrategy.default

    /** Sets global defaults for snackbars. */
    fun snack(block: SnackConfig.() -> Unit) { snackConfig.apply(block) }

    /** Sets global defaults for toasts. */
    fun toast(block: ToastConfig.() -> Unit) { toastConfig.apply(block) }

    /** Sets global defaults for dialogs. */
    fun dialog(block: DialogConfig.() -> Unit) { dialogConfig.apply(block) }

    /** Sets global defaults for loading overlays. */
    fun loading(block: LoadingConfig.() -> Unit) { loadingConfig.apply(block) }

    /** Customizes the color scheme. */
    fun theme(block: ThemeConfig.() -> Unit) { theme.apply(block) }

    /**
     * Sets the queue strategy for toast, snack, and dialog signals.
     * - [QueueStrategy.Independent]: each type has its own queue (default).
     * - [QueueStrategy.GlobalSequential]: all types share a single queue.
     */
    fun setQueueStrategy(strategy: QueueStrategy) { queueStrategy = strategy }
}