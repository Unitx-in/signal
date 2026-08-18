package com.unitx.signal_core.handler

import android.app.Activity
import com.unitx.signal_core.contract.config.ToastConfig
import com.unitx.signal_core.helper.DismissController
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.activity.ActivityBinding
import com.unitx.signal_core.activity.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.view.ToastViewManager

internal class ToastHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: ToastConfig,
    private val queue: SignalQueue,
    private val viewManager: ToastViewManager,
    private val animator: SignalAnimator
) {

    private var currentConfig: ToastConfig = globalConfig.copy()
    private var binding: ActivityBinding? = null
    private val dismissController = DismissController()

    private var currentTag: String? = null

    val isShowing: Boolean
        get() = viewManager.isShowing || dismissController.isBusy

    fun show(activity: Activity, message: String) = show(activity, message) {}

    fun show(activity: Activity, message: String, block: ToastConfig.() -> Unit) {
        ensureMainThread()
        val config = globalConfig.copy().apply(block)
        config.message = message

        if (config.tag != null && config.tag == currentTag) return

        currentTag = config.tag

        queue.enqueue(
            show = { display(activity, config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(activity: Activity, config: ToastConfig) {
        dismissController.reset()
        currentConfig = config
        binding = activityProvider.bindTo(activity) { onOwningActivityDestroyed() }

        val attached = viewManager.attach(activity, config) { dismiss() }
        if (!attached) {
            clearBinding()
            queue.next()
            return
        }
        val container = viewManager.container ?: return
        animator.fadeIn(container)
        config.onShown?.invoke()
        if (config.dismissOnTap) {
            container.setOnClickListener { dismiss() }
        }
        dismissController.scheduleAutoDismiss(config.duration) { dismiss() }
    }

    fun dismiss() = dismissController.dismissOnce { complete ->
        currentTag = null
        clearBinding()

        val container = viewManager.container ?: run {
            complete()
            queue.next()
            return@dismissOnce
        }
        animator.fadeOut(container) {
            complete()
            currentConfig.onDismissed?.invoke()
            queue.next()
        }
    }

    private fun onOwningActivityDestroyed() = dismissController.dismissOnce { complete ->
        clearBinding()
        viewManager.release()
        complete()
        queue.clear()
    }

    private fun clearBinding() {
        binding?.unbind()
        binding = null
    }
}