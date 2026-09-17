package com.unitx.signal_core.queue

/**
 * Controls how Toast, Snack, Dialog, and Notification are queued when multiple signals
 * are triggered. Configured via [SignalConfig.setQueueStrategy].
 *
 * Loading is not governed by this strategy — it always runs on its own independent
 * queue, since it reflects an in-flight operation rather than an advisory message.
 */
enum class QueueStrategy {
    /** Every governed signal type manages its own independent queue. */
    Independent,

    /** Every governed signal type shares one global queue. */
    Global,

    /**
     * Toast, Snack, and Dialog share one global queue.
     * Notification is exempted onto its own independent queue, since it's an ephemeral
     * banner rather than an advisory message that should wait its turn.
     */
    GlobalWithExemptRequired;

    companion object {
        val default = GlobalWithExemptRequired
    }
}