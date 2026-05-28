package com.unitx.signal_core.handler.toast

import android.app.Activity
import com.unitx.signal_core.common.config.base.ToastConfig
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue

class ToastHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: ToastConfig,
    private val queue: SignalQueue,
    private val viewManager: ToastViewManager,
    private val animator: ToastAnimator,
    private val scheduler: ToastDismissScheduler
) {

    private var currentConfig: ToastConfig = globalConfig.copy()
    private val destroyListener: (Activity) -> Unit = { release() }

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(message: String) = show(message) {}

    fun show(message: String, block: ToastConfig.() -> Unit) {
        val config = globalConfig.copy().apply(block)
        currentConfig = config

        queue.enqueue(
            show = { display(message, config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(message: String, config: ToastConfig) {
        val attached = viewManager.attach(config, message)
        if (!attached) return
        val container = viewManager.container ?: return
        animator.fadeIn(container)
        scheduler.schedule(config.duration) { dismiss() }
    }

    fun dismiss() {
        scheduler.cancel()
        val container = viewManager.container ?: run { queue.next(); return }
        animator.fadeOut(container) {
            queue.next()
        }
    }

    private fun release() {
        activityProvider.removeOnDestroyListener(destroyListener)
        scheduler.cancel()
        viewManager.release()
    }
}