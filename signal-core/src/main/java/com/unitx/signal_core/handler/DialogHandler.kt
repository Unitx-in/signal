package com.unitx.signal_core.handler

import android.app.Activity
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.helper.BackPressHandler
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.view.dialog.DialogViewManager

internal class DialogHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: DialogConfig,
    private val queue: SignalQueue,
    private val viewManager: DialogViewManager,
    private val animator: SignalAnimator,
    private val scheduler: SignalDismissScheduler
) {

    private var currentConfig: DialogConfig = DialogConfig()
    private val backPressHandler: BackPressHandler = BackPressHandler(activityProvider)
    private val destroyListener: (Activity) -> Unit = { release() }

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(block: DialogConfig.() -> Unit) {
        ensureMainThread()
        val config = globalConfig.copy().apply(block)

        queue.enqueue(
            show = { display(config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    private fun display(config: DialogConfig) {
        currentConfig = config
        val attached = viewManager.attach(config, onDismiss = { dismiss() })
        if (!attached) return

        val card = viewManager.container ?: return
        animator.scaleIn(card)
        config.onShown?.invoke()

        if (config.autoDismiss) {
            scheduler.schedule(config.autoDismissDuration) { dismiss() }
        }

        if (config.cancelable) {
            backPressHandler.register { dismiss() }
        }
    }

    fun dismiss() {
        backPressHandler.unregister()
        scheduler.cancel()

        val card = viewManager.container ?: run { queue.next(); return }
        animator.scaleOut(card) {
            viewManager.release {
                currentConfig.onDismissed?.invoke()
                queue.next()
            }
        }
    }

    private fun release() {
        activityProvider.removeOnDestroyListener(destroyListener)
        backPressHandler.unregister()
        scheduler.cancel()
        viewManager.release()
        queue.clear()
    }
}