package com.unitx.signal_core.helper

import android.os.Handler
import android.os.Looper

/**
 * Owns the full dismiss lifecycle for one signal handler's currently shown
 * instance: the auto-dismiss timer, idempotency (so a dismiss triggered
 * twice — a fast tap + an in-flight scheduler callback, or an activity
 * destroy racing a manual dismiss — only tears down once), and whether a
 * teardown animation is still in flight after the container itself is
 * already visually gone.
 *
 * A handler's `isShowing` must OR in [isBusy] — the container's own
 * visibility flips to GONE before the full release()/dim-fade sequence
 * finishes, and during that gap the queue (or a re-entrant show()) must
 * still treat this signal as "showing" or it will let the next entry jump
 * ahead and steal the still-attached view.
 */
internal class DismissController {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var dismissRunnable: Runnable? = null

    private var dismissing = false
    private var tearingDown = false

    /** True from the moment a dismiss starts until teardown's `complete()` is called. */
    val isBusy: Boolean
        get() = tearingDown

    /** Call when a new instance is shown — arms the guard and clears any stale timer. */
    fun reset() {
        cancelAutoDismiss()
        dismissing = false
        tearingDown = false
    }

    /** Schedules [onDismiss] after [duration]ms. Replaces any previously scheduled timer. */
    fun scheduleAutoDismiss(duration: Long, onDismiss: () -> Unit) {
        cancelAutoDismiss()
        dismissRunnable = Runnable { onDismiss() }
        mainHandler.postDelayed(dismissRunnable!!, duration)
    }

    /** Cancels a pending auto-dismiss timer, if any. */
    fun cancelAutoDismiss() {
        dismissRunnable?.let { mainHandler.removeCallbacks(it) }
        dismissRunnable = null
    }

    /**
     * Runs [block] only on the first call since the last [reset] — later calls
     * (a double tap, a scheduler callback racing a manual dismiss, an activity
     * destroy racing an in-flight dismiss) are silent no-ops.
     *
     * [block] receives a `complete` callback that MUST be invoked once the full
     * teardown (including any release()/fade animation) has actually finished —
     * that's what flips [isBusy] back to false. Calling `complete` more than
     * once is safe (idempotent no-op after the first call). Always cancels any
     * pending auto-dismiss timer up front, since any dismiss path supersedes a
     * scheduled one. `block` is expected not to throw — an exception here will
     * leave [isBusy] stuck true.
     */
    inline fun dismissOnce(block: (complete: () -> Unit) -> Unit) {
        if (dismissing) return
        dismissing = true
        tearingDown = true
        cancelAutoDismiss()

        var completed = false
        val complete = {
            if (!completed) {
                completed = true
                tearingDown = false
            }
        }

        block(complete)
    }

    
}