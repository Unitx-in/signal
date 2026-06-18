package com.unitx.signal_core.handler

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.unitx.signal_core.contract.config.LoadingConfig
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.view.LoadingViewManager

internal class LoadingHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: LoadingConfig,
    private val viewManager: LoadingViewManager,
    private val animator: SignalAnimator
) {

    private var currentConfig: LoadingConfig = LoadingConfig()
    private var backPressCallback: OnBackPressedCallback? = null
    private val destroyListener: (Activity) -> Unit = { release() }

    init {
        activityProvider.addOnDestroyListener(destroyListener)
    }

    val isShowing: Boolean
        get() = viewManager.isShowing

    fun show(block: LoadingConfig.() -> Unit = {}) {
        ensureMainThread()
        if (isShowing) return
        val config = globalConfig.copy().apply(block)
        display(config)
    }

    fun updateProgress(progress: Int, message: String? = null) {
        ensureMainThread()
        if (!isShowing) return
        currentConfig.progress = progress
        currentConfig.progressMessage = message
        viewManager.updateProgress(currentConfig)
    }

    fun dismiss() {
        backPressCallback?.remove()
        backPressCallback = null

        val container = viewManager.container ?: return
        animator.scaleOut(container) {
            viewManager.release {
                currentConfig.onDismissed?.invoke()
            }
        }
    }

    private fun display(config: LoadingConfig) {
        currentConfig = config
        val attached = viewManager.attach(config, onDismiss = {
            config.onCancelled?.invoke()
            dismiss()
        })
        if (!attached) return

        val container = viewManager.container ?: return
        animator.scaleIn(container)
        config.onShown?.invoke()

        if (config.dismissOnBackPress) {
            val activity = activityProvider.current() as? ComponentActivity ?: return
            backPressCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { dismiss() }
            }
            activity.onBackPressedDispatcher.addCallback(activity, backPressCallback!!)
        }
    }

    private fun release() {
        activityProvider.removeOnDestroyListener(destroyListener)
        backPressCallback?.remove()
        backPressCallback = null
        viewManager.release()
    }
}