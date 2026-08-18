package com.unitx.signal_core.handler

import android.app.Activity
import com.unitx.signal_core.contract.config.SnackConfig
import com.unitx.signal_core.helper.BackPressHandler
import com.unitx.signal_core.helper.DismissController
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.activity.ActivityBinding
import com.unitx.signal_core.activity.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.view.SnackViewManager

internal class SnackHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: SnackConfig,
    private val queue: SignalQueue,
    private val viewManager: SnackViewManager,
    private val animator: SignalAnimator
) {

    private var currentConfig: SnackConfig = globalConfig.copy()
    private var binding: ActivityBinding? = null
    private val backPressHandler: BackPressHandler = BackPressHandler()
    private val dismissController = DismissController()

    private var currentTag: String? = null

    val isShowing: Boolean
        get() = viewManager.isShowing || dismissController.isBusy

    fun show(activity: Activity, message: String) = show(activity, message) {}

    fun show(activity: Activity, message: String, block: SnackConfig.() -> Unit) {
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

    private fun display(activity: Activity, config: SnackConfig) {
        dismissController.reset()
        currentConfig = config
        binding = activityProvider.bindTo(activity) { onOwningActivityDestroyed() }

        val attached = viewManager.attach(activity, config, onDismiss = { dismiss() })
        if (!attached) {
            clearBinding()
            queue.next()
            return
        }

        if (currentConfig.dismissOnBackPress) {
            backPressHandler.register(activity) { dismiss() }
        }

        val container = viewManager.container ?: return
        animator.slideIn(container, config.position)
        config.onShown?.invoke()
        if (!config.persistent) {
            dismissController.scheduleAutoDismiss(config.duration) { dismiss() }
        }
    }

    fun dismiss() = dismissController.dismissOnce { complete ->
        currentTag = null
        clearBinding()
        backPressHandler.unregister()

        val container = viewManager.container ?: run {
            complete()
            queue.next()
            return@dismissOnce
        }
        animator.slideOut(container, currentConfig.position) {
            complete()
            currentConfig.onDismissed?.invoke()
            queue.next()
        }
    }

    private fun onOwningActivityDestroyed() = dismissController.dismissOnce { complete ->
        clearBinding()
        backPressHandler.unregister()
        viewManager.release()
        complete()
        queue.clear()
    }

    private fun clearBinding() {
        binding?.unbind()
        binding = null
    }
}