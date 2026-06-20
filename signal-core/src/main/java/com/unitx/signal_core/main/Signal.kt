package com.unitx.signal_core.main

import android.app.Application
import com.unitx.signal_core.contract.config.DialogConfig
import com.unitx.signal_core.contract.config.LoadingConfig
import com.unitx.signal_core.contract.config.SignalConfig
import com.unitx.signal_core.contract.config.SnackConfig
import com.unitx.signal_core.contract.config.ToastConfig
import com.unitx.signal_core.core.SignalCore

/**
 * The main entry point for the Signal UI feedback library.
 *
 * Signal provides a unified API for displaying toasts, snackbars, dialogs,
 * and loading overlays. It must be initialized once — typically in your
 * [Application] subclass — before any signals can be shown.
 *
 * ## Setup
 * ```kotlin
 * class MyApp : Application() {
 *     override fun onCreate() {
 *         super.onCreate()
 *         Signal.createCore(this) {
 *             toast { duration = 3000L }
 *             snack { position = SnackPosition.Top }
 *             dialog { cancelable = true }
 *             loading { simpleLoading = true }
 *             setQueueStrategy(QueueStrategy.GlobalSequential)
 *         }
 *     }
 * }
 * ```
 *
 * ## Usage
 * ```kotlin
 * Signal.toast("Hello!")
 * Signal.snack("Changes saved") { type = SnackType.Success }
 * Signal.dialog { title = "Confirm?"; positive("Yes") { doIt() } }
 * Signal.loading { title = "Uploading..." }
 * Signal.dismissLoading()
 * ```
 */
object Signal {

    private lateinit var core: SignalCore

    /**
     * Initializes the Signal library. Must be called once before using any other Signal functions,
     * typically in [Application.onCreate].
     *
     * @param app The application instance, used to observe activity lifecycle.
     * @param block Optional configuration block to set global defaults for all signal types.
     *              See [SignalConfig] for available options.
     */
    fun createCore(app: Application, block: SignalConfig.() -> Unit = {}) {
        val config = SignalConfig().apply(block)
        core = SignalCore(app, config)
    }

    /** Shows a toast with the given [message] using global default configuration. */
    fun toast(message: String) = core.toastHandler.show(message)

    /**
     * Shows a toast with the given [message] and a custom [block]. See [ToastConfig] for options.
     */
    fun toast(message: String, block: ToastConfig.() -> Unit) =
        core.toastHandler.show(message, block)

    /** Shows a snackbar with the given [message] using global default configuration. */
    fun snack(message: String) = core.snackHandler.show(message)

    /**
     * Shows a snackbar with the given [message] and a custom [block]. See [SnackConfig] for options.
     */
    fun snack(message: String, block: SnackConfig.() -> Unit) {
        return core.snackHandler.show(message, block)
    }

    /**
     * Shows a dialog configured via [block]. Dialogs are queued — if one is already visible,
     * the new one waits until the current dialog is dismissed. See [DialogConfig] for options.
     */
    fun dialog(block: DialogConfig.() -> Unit) = core.dialogHandler.show(block)

    /**
     * Programmatically dismisses the currently visible dialog, if any.
     * Triggers [DialogConfig.onDismissed] and advances the dialog queue.
     */
    fun dismissDialog() {
        core.dialogHandler.dismiss()
    }

    /**
     * Shows a loading overlay configured via [block]. Only one overlay can be shown at a time —
     * calling this while one is already visible has no effect. See [LoadingConfig] for options.
     */
    fun loading(block: LoadingConfig.() -> Unit = {}) = core.loadingHandler.show(block)

    /**
     * Updates the progress and optional [message] on a visible determinate loading overlay.
     *
     * @param progress New progress value (0–100).
     * @param message Optional label shown alongside the percentage, e.g. `"42% · Uploading files"`.
     */
    fun updateProgress(progress: Int, message: String? = null) =
        core.loadingHandler.updateProgress(progress, message)

    /**
     * Dismisses the currently visible loading overlay, if any.
     * Triggers [LoadingConfig.onDismissed].
     */
    fun dismissLoading() = core.loadingHandler.dismiss()
}