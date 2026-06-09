package com.unitx.signal_core.handler.dialog

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.unitx.signal_core.common.config.base.DialogConfig
import com.unitx.signal_core.common.helper.SignalAnimator
import com.unitx.signal_core.common.helper.SignalDismissScheduler
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue

class DialogHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: DialogConfig,
    private val queue: SignalQueue,
    private val viewManager: DialogViewManager,
    private val animator: SignalAnimator,
    private val scheduler: SignalDismissScheduler
) {

    private var currentConfig: DialogConfig = DialogConfig()
    private var backPressCallback: OnBackPressedCallback? = null
    private val destroyListener: (Activity) -> Unit = { release() }

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(block: DialogConfig.() -> Unit) {
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

        if (config.autoDismiss) {
            scheduler.schedule(config.autoDismissDuration) { dismiss() }
        }

        if (config.cancelable) {
            val activity = activityProvider.current() as? ComponentActivity ?: return
            backPressCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { dismiss() }
            }
            activity.onBackPressedDispatcher.addCallback(activity, backPressCallback!!)
        }
    }

    fun dismiss() {
        backPressCallback?.remove()
        backPressCallback = null
        scheduler.cancel()

        val card = viewManager.container ?: run { queue.next(); return }
        animator.scaleOut(card) {
            viewManager.release {
                queue.next()
            }
        }
    }

    private fun release() {
        activityProvider.removeOnDestroyListener(destroyListener)
        backPressCallback?.remove()
        backPressCallback = null
        scheduler.cancel()
        viewManager.release()
        queue.clear()
    }
}