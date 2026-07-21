package com.unitx.signal_core.contract.config

import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.queue.QueueStrategy
import com.unitx.signal_core.theme.ThemeConfig
import com.unitx.signal_core.interop.JavaUnitCallback

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

    internal val notifConfig = NotificationConfig()

    /** Sets global defaults for notifications. */
    fun notif(block: NotificationConfig.() -> Unit) { notifConfig.apply(block) }

    /**
     * Java-friendly overload of [notif]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Sets global defaults for notifications.
     */
    fun notif(block: JavaUnitCallback<NotificationConfig>) { notifConfig.apply { block.invoke(this) } }

    /** Sets global defaults for snackbars. */
    fun snack(block: SnackConfig.() -> Unit) { snackConfig.apply(block) }

    /**
     * Java-friendly overload of [snack]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Sets global defaults for snackbars.
     */
    fun snack(block: JavaUnitCallback<SnackConfig>) { snackConfig.apply { block.invoke(this) } }

    /** Sets global defaults for toasts. */
    fun toast(block: ToastConfig.() -> Unit) { toastConfig.apply(block) }

    /**
     * Java-friendly overload of [toast]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Sets global defaults for toasts.
     */
    fun toast(block: JavaUnitCallback<ToastConfig>) { toastConfig.apply { block.invoke(this) } }

    /** Sets global defaults for dialogs. */
    fun dialog(block: DialogConfig.() -> Unit) { dialogConfig.apply(block) }

    /**
     * Java-friendly overload of [dialog]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Sets global defaults for dialogs.
     */
    fun dialog(block: JavaUnitCallback<DialogConfig>) { dialogConfig.apply { block.invoke(this) } }

    /** Sets global defaults for loading overlays. */
    fun loading(block: LoadingConfig.() -> Unit) { loadingConfig.apply(block) }

    /**
     * Java-friendly overload of [loading]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Sets global defaults for loading overlays.
     */
    fun loading(block: JavaUnitCallback<LoadingConfig>) { loadingConfig.apply { block.invoke(this) } }

    /** Customizes the color scheme. */
    fun theme(block: ThemeConfig.() -> Unit) { theme.apply(block) }

    /**
     * Java-friendly overload of [theme]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Customizes the color scheme.
     */
    fun theme(block: JavaUnitCallback<ThemeConfig>) { theme.apply { block.invoke(this) } }

    /**
     * Sets the queue strategy for toast, snack, and dialog signals.
     * - [QueueStrategy.Independent]: each type has its own queue (default).
     * - [QueueStrategy.GlobalSequential]: all types share a single queue.
     */
    fun setQueueStrategy(strategy: QueueStrategy) { queueStrategy = strategy }
}