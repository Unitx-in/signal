package com.unitx.signal_core.handler

import android.app.Activity
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.helper.BackPressHandler
import com.unitx.signal_core.helper.DismissController
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.activity.ActivityBinding
import com.unitx.signal_core.activity.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.view.dialog.DialogViewManager

internal class DialogHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: DialogConfig,
    private val queue: SignalQueue,
    private val viewManager: DialogViewManager,
    private val animator: SignalAnimator
) {

    private var currentConfig: DialogConfig = DialogConfig()
    private var binding: ActivityBinding? = null
    private val backPressHandler: BackPressHandler = BackPressHandler()
    private val dismissController = DismissController()

    val isShowing: Boolean
        get() = viewManager.isShowing || dismissController.isBusy

    fun show(activity: Activity, block: DialogConfig.() -> Unit) {
        ensureMainThread()
        val config = globalConfig.copy().apply(block)

        queue.enqueue(
            show = { display(activity, config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(activity: Activity, config: DialogConfig) {
        dismissController.reset()
        currentConfig = config
        binding = activityProvider.bindTo(activity) { onOwningActivityDestroyed() }

        val attached = viewManager.attach(activity, config, onDismiss = { dismiss() })
        if (!attached) {
            clearBinding()
            queue.next()
            return
        }

        val card = viewManager.container ?: return
        animator.scaleIn(card)
        config.onShown?.invoke()

        if (config.autoDismiss) {
            dismissController.scheduleAutoDismiss(config.autoDismissDuration) { dismiss() }
        }

        backPressHandler.register(activity) {
            if (config.cancelable) dismiss()
        }
    }

    fun dismiss() = dismissController.dismissOnce { complete ->
        clearBinding()
        backPressHandler.unregister()

        val card = viewManager.container ?: run {
            complete()
            queue.next()
            return@dismissOnce
        }

        animator.scaleOut(card) {
            viewManager.release {
                complete()
                currentConfig.onDismissed?.invoke()
                queue.next()
            }
        }
    }

    private fun onOwningActivityDestroyed() = dismissController.dismissOnce { complete ->
        clearBinding()
        backPressHandler.unregister()
        viewManager.release { complete() }
        queue.clear()
    }

    private fun clearBinding() {
        binding?.unbind()
        binding = null
    }
}