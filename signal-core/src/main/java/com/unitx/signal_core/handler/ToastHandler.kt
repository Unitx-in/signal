package com.unitx.signal_core.handler

import android.app.Activity
import com.unitx.signal_core.contract.config.ToastConfig
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.view.ToastViewManager

internal class ToastHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: ToastConfig,
    private val queue: SignalQueue,
    private val viewManager: ToastViewManager,
    private val animator: SignalAnimator,
    private val scheduler: SignalDismissScheduler
) {

    private var currentConfig: ToastConfig = globalConfig.copy()
    private val destroyListener: (Activity) -> Unit = { release() }

    private var currentTag: String? = null

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(message: String) = show(message) {}

    fun show(message: String, block: ToastConfig.() -> Unit) {
        ensureMainThread()
        val config = globalConfig.copy().apply(block)

        if (config.tag != null && config.tag == currentTag) return

        currentTag = config.tag

        queue.enqueue(
            show = { display(message, config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(message: String, config: ToastConfig) {
        currentConfig = config
        val attached = viewManager.attach(config, message)
        if (!attached) return
        val container = viewManager.container ?: return
        animator.fadeIn(container)
        config.onShown?.invoke()
        if (config.dismissOnTap) {
            container.setOnClickListener { dismiss() }
        }
        scheduler.schedule(config.duration) { dismiss() }
    }

    fun dismiss() {
        currentTag = null
        scheduler.cancel()
        val container = viewManager.container ?: run { queue.next(); return }
        animator.fadeOut(container) {
            currentConfig.onDismissed?.invoke()
            queue.next()
        }
    }

    private fun release() {
        activityProvider.removeOnDestroyListener(destroyListener)
        scheduler.cancel()
        viewManager.release()
        queue.clear()
    }
}