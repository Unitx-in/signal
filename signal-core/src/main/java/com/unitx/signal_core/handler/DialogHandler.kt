package com.unitx.signal_core.handler

import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.helper.BackPressHandler
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
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
    private val animator: SignalAnimator,
    private val scheduler: SignalDismissScheduler
) {

    private var currentConfig: DialogConfig = DialogConfig()
    private var binding: ActivityBinding? = null
    private val backPressHandler: BackPressHandler = BackPressHandler(activityProvider)

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
        val newBinding = activityProvider.bindToCurrentActivity { onOwningActivityDestroyed() }
            ?: run {
                queue.next() // no foreground activity to attach to — skip this dialog, advance queue
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

        val card = viewManager.container ?: return
        animator.scaleIn(card)
        config.onShown?.invoke()

        if (config.autoDismiss) {
            scheduler.schedule(config.autoDismissDuration) { dismiss() }
        }

        backPressHandler.register {
            if (config.cancelable) dismiss()
        }
    }

    fun dismiss() {
        clearBinding()
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

    /** Called only when the specific activity we attached to is destroyed. */
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