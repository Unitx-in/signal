package com.unitx.signal_core.handler.snack

import android.app.Activity
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.common.config.base.SnackConfig
import com.unitx.signal_core.provider.ActivityProvider

class SnackHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: SnackConfig,
    private val queue: SignalQueue,
    private val viewManager: SnackViewManager,
    private val animator: SnackAnimator,
    private val scheduler: SnackDismissScheduler
) {

    private var currentConfig: SnackConfig = globalConfig.copy()
    private val destroyListener: (Activity) -> Unit = { release() }

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(message: String) = show(message) {}

    fun show(message: String, block: SnackConfig.() -> Unit) {
        val config = globalConfig.copy().apply(block)
        config.message = message

        queue.enqueue(
            show = { display(config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(localConfig: SnackConfig) {
        currentConfig = localConfig
        val attached = viewManager.attach(localConfig, onDismiss = { dismiss() })
        if (!attached) return

        val container = viewManager.container ?: return
        animator.slideIn(container, localConfig.position)
        scheduler.schedule(localConfig.duration) { dismiss() }
    }

    fun dismiss() {
        scheduler.cancel()
        val container = viewManager.container ?: run { queue.next(); return }
        animator.slideOut(container, currentConfig.position) {
            queue.next()
        }
    }

    private fun release() {
        activityProvider.removeOnDestroyListener(destroyListener)
        scheduler.cancel()
        viewManager.release()
    }
}