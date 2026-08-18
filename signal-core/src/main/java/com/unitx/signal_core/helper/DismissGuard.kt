package com.unitx.signal_core.helper

/**
 * Prevents a dismiss/teardown path from running more than once for the same
 * shown instance of a signal (toast/snack/dialog/notif/loading).
 *
 * Handlers reuse a single ViewManager instance across every show() call, so
 * without this guard, two dismiss triggers landing close together (e.g. an
 * auto-dismiss timer racing a manual tap, or a button tap racing
 * onOwningActivityDestroyed) can both pass the "is anything showing" check
 * before the first one finishes tearing down — double-firing queue.next()
 * and corrupting which signal the queue thinks is current.
 *
 * Usage:
 * ```
 * private val dismissGuard = DismissGuard()
 *
 * private fun display(...) {
 *     dismissGuard.reset()
 *     ...
 * }
 *
 * fun dismiss() {
 *     dismissGuard.runOnce {
 *         ...teardown...
 *     }
 * }
 * ```
 */
internal class DismissGuard {
    private var dismissing = false

    /** Call at the start of display()/show() — arms the guard for a new shown instance. */
    fun reset() {
        dismissing = false
    }

    /** Runs [block] only if this is the first call since the last [reset]. Subsequent calls are no-ops. */
    inline fun runOnce(block: () -> Unit) {
        if (dismissing) return
        dismissing = true
        block()
    }
}