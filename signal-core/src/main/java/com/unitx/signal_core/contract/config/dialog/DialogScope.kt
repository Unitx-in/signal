package com.unitx.signal_core.contract.config.dialog

/**
 * Receiver scope passed into positive/negative/neutral button callbacks.
 *
 * By default the dialog dismisses after a button action runs. Call [prevent]
 * to keep it open (e.g. for async work or validation), and [dismiss] later
 * once ready to close it.
 *
 * Usage:
 * ```
 * positive("Submit") {
 *     prevent()
 *     scope.launch {
 *         if (api.submit()) dismiss()
 *     }
 * }
 * ```
 */
class DialogScope {
    internal var shouldDismiss: Boolean = true

    /** Marks the dialog to dismiss (default behavior; only needed after a prior [prevent]). */
    fun dismiss() { shouldDismiss = true }

    /** Prevents the dialog from auto-dismissing after this callback returns. */
    fun prevent() { shouldDismiss = false }
}