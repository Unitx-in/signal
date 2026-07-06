package com.unitx.signal_core.handler

import com.unitx.signal_core.contract.config.SnackConfig
import com.unitx.signal_core.helper.BackPressHandler
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
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
    private val animator: SignalAnimator,
    private val scheduler: SignalDismissScheduler
) {

    private var currentConfig: SnackConfig = globalConfig.copy()
    private var binding: ActivityBinding? = null

    private val backPressHandler: BackPressHandler = BackPressHandler(activityProvider)

    private var currentTag: String? = null

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(message: String) = show(message) {}

    fun show(message: String, block: SnackConfig.() -> Unit) {
        ensureMainThread()
        val config = globalConfig.copy().apply(block)
        config.message = message

        if (config.tag != null && config.tag == currentTag) return
        currentTag = config.tag

        queue.enqueue(
            show = { display(config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(config: SnackConfig) {
        val newBinding = activityProvider.bindToCurrentActivity { onOwningActivityDestroyed() }
            ?: run {
                queue.next() // no foreground activity to attach to — skip, advance queue
                return
            }

        currentConfig = config
        binding = newBinding

        val attached = viewManager.attach(config, onDismiss = { dismiss() })
        if (!attached) {
            clearBinding()
            queue.next()
            return
        }

        if (currentConfig.dismissOnBackPress) {
            backPressHandler.register { dismiss() }
        }

        val container = viewManager.container ?: return
        animator.slideIn(container, config.position)
        config.onShown?.invoke()
        if (!config.persistent) {
            scheduler.schedule(config.duration) { dismiss() }
        }
    }

    fun dismiss() {
        currentTag = null
        clearBinding()
        backPressHandler.unregister()
        scheduler.cancel()
        val container = viewManager.container ?: run { queue.next(); return }
        animator.slideOut(container, currentConfig.position) {
            currentConfig.onDismissed?.invoke()
            queue.next()
        }
    }

    private fun onOwningActivityDestroyed() {
        clearBinding()
        backPressHandler.unregister()
        scheduler.cancel()
        viewManager.release()
        queue.clear()
    }

    private fun clearBinding() {
        binding?.unbind()
        binding = null
    }
}