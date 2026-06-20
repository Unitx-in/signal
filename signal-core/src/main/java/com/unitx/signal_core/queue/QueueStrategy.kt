package com.unitx.signal_core.queue

/**
 * Controls how toasts, snackbars, and dialogs are queued when multiple signals are triggered.
 * Configured via [SignalConfig.setQueueStrategy].
 */
enum class QueueStrategy {
    /**
     * Each signal type (Toast, Snack, Dialog) manages its own independent queue.
     * A toast inside a dialog's action will show immediately without waiting for the dialog to dismiss.
     */
    Independent,

    /**
     * All signal types share a single global queue.
     * Nothing will show until the previous signal — regardless of type — is fully dismissed.
     */
    GlobalSequential;

    companion object {
        val default = Independent
    }
}