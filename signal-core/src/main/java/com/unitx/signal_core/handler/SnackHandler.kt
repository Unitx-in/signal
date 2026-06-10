package com.unitx.signal_core.handler

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.unitx.signal_core.contract.config.SnackConfig
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.provider.ActivityProvider
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
    private val destroyListener: (Activity) -> Unit = { release() }

    private var backPressCallback: OnBackPressedCallback? = null

    private var currentTag: String? = null

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

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
        currentConfig = config
        val attached = viewManager.attach(config, onDismiss = { dismiss() })
        if (!attached) return

        if (currentConfig.dismissOnBackPress) {
            val activity = activityProvider.current() as? ComponentActivity
            activity?.let {
                backPressCallback = object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() { dismiss() }
                }
                it.onBackPressedDispatcher.addCallback(it, backPressCallback!!)
            }
        }

        val container = viewManager.container ?: return
        animator.slideIn(container, config.position)
        config.onShown?.invoke()
        scheduler.schedule(config.duration) { dismiss() }
    }

    fun dismiss() {
        currentTag = null
        backPressCallback?.remove()
        backPressCallback = null
        scheduler.cancel()
        val container = viewManager.container ?: run { queue.next(); return }
        animator.slideOut(container, currentConfig.position) {
            currentConfig.onDismissed?.invoke()
            queue.next()
        }
    }

    private fun release() {
        backPressCallback?.remove()
        backPressCallback = null
        activityProvider.removeOnDestroyListener(destroyListener)
        scheduler.cancel()
        viewManager.release()
        queue.clear()
    }
}