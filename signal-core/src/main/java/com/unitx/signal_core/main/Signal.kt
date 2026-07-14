package com.unitx.signal_core.main

import android.app.Activity
import android.app.Application
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.contract.config.LoadingConfig
import com.unitx.signal_core.contract.config.NotificationConfig
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
 * Every show function requires the [Activity] to attach to, passed explicitly
 * by the caller. This removes any dependency on "current foreground activity"
 * timing — a signal shown from onCreate(), onStart(), or onResume() all behave
 * identically, since the target activity is never inferred.
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
 * Signal.toast(this, "Hello!")
 * Signal.snack(this, "Changes saved") { type = SnackType.Success }
 * Signal.dialog(this) { title = "Confirm?"; positive("Yes") { doIt() } }
 * Signal.loading(this) { title = "Uploading..." }
 * Signal.dismissLoading()
 * ```
 */
object Signal {

    private lateinit var core: SignalCore

    /**
     * Safe accessor for [core]. Throws a descriptive [IllegalStateException] if
     * [createCore] hasn't been called yet, instead of letting the raw
     * [UninitializedPropertyAccessException] leak out with no guidance.
     */
    private val safeCore: SignalCore
        get() {
            check(::core.isInitialized) {
                "Signal has not been initialized. Call Signal.createCore(application) " +
                        "in your Application.onCreate() before calling any other Signal function."
            }
            return core
        }

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

    /** Returns whether [createCore] has already been called. */
    fun isInitialized(): Boolean = ::core.isInitialized

    /** Shows a toast on [activity] with the given [message] using global default configuration. */
    fun toast(activity: Activity, message: String) = safeCore.toastHandler.show(activity, message)

    /**
     * Shows a toast on [activity] with the given [message] and a custom [block].
     * See [ToastConfig] for options.
     */
    fun toast(activity: Activity, message: String, block: ToastConfig.() -> Unit) =
        safeCore.toastHandler.show(activity, message, block)

    /** Shows a snackbar on [activity] with the given [message] using global default configuration. */
    fun snack(activity: Activity, message: String) = safeCore.snackHandler.show(activity, message)

    /**
     * Shows a snackbar on [activity] with the given [message] and a custom [block].
     * See [SnackConfig] for options.
     */
    fun snack(activity: Activity, message: String, block: SnackConfig.() -> Unit) {
        return safeCore.snackHandler.show(activity, message, block)
    }

    /**
     * Shows a dialog on [activity], configured via [block]. Dialogs are queued — if one
     * is already visible, the new one waits until the current dialog is dismissed.
     * See [DialogConfig] for options.
     */
    fun dialog(activity: Activity, block: DialogConfig.() -> Unit) =
        safeCore.dialogHandler.show(activity, block)

    /**
     * Programmatically dismisses the currently visible dialog, if any.
     * Triggers [DialogConfig.onDismissed] and advances the dialog queue.
     */
    fun dismissDialog() {
        safeCore.dialogHandler.dismiss()
    }

    /**
     * Shows a loading overlay on [activity], configured via [block]. Only one overlay can
     * be shown at a time — calling this while one is already visible has no effect.
     * See [LoadingConfig] for options.
     */
    fun loading(activity: Activity, block: LoadingConfig.() -> Unit = {}) =
        safeCore.loadingHandler.show(activity, block)

    /**
     * Updates the progress and optional [message] on a visible determinate loading overlay.
     *
     * @param progress New progress value (0–100).
     * @param message Optional label shown alongside the percentage, e.g. `"42% · Uploading files"`.
     */
    fun updateProgress(progress: Int, message: String? = null) =
        safeCore.loadingHandler.updateProgress(progress, message)

    /**
     * Dismisses the currently visible loading overlay, if any.
     * Triggers [LoadingConfig.onDismissed].
     */
    fun dismissLoading() = safeCore.loadingHandler.dismiss()

    /** Shows a notification on [activity], configured via [block]. See [NotificationConfig] for options. */
    fun notif(activity: Activity, block: NotificationConfig.() -> Unit) =
        safeCore.notifHandler.show(activity, block)

    /** Dismisses the currently visible notification, if any. Triggers [NotificationConfig.onDismissed]. */
    fun dismissNotif() = safeCore.notifHandler.dismiss()
}