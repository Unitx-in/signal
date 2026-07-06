package com.unitx.signal_core.handler

import com.unitx.signal_core.contract.config.LoadingConfig
import com.unitx.signal_core.helper.BackPressHandler
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.ensureMainThread
import com.unitx.signal_core.view.loading.ILoadingViewManager
import com.unitx.signal_core.view.loading.LoadingViewManager
import com.unitx.signal_core.view.loading.SimpleLoadingViewManager
import com.unitx.signal_core.activity.ActivityBinding
import com.unitx.signal_core.activity.ActivityProvider

internal class LoadingHandler(
    private val activityProvider: ActivityProvider,
    private val globalConfig: LoadingConfig,
    private val advancedViewManager: LoadingViewManager,
    private val simpleViewManager: SimpleLoadingViewManager,
    private val animator: SignalAnimator
) {
    private var activeManager: ILoadingViewManager = advancedViewManager
    private var currentConfig: LoadingConfig = LoadingConfig()
    private val backPressHandler = BackPressHandler(activityProvider)
    private var binding: ActivityBinding? = null

    val isShowing: Boolean
        get() = activeManager.isShowing

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
        activeManager.updateProgress(currentConfig)
    }

    fun dismiss() {
        clearBinding()
        backPressHandler.unregister()
        val container = activeManager.container ?: return
        animator.scaleOut(container) {
            activeManager.release {
                currentConfig.onDismissed?.invoke()
            }
        }
    }

    private fun display(config: LoadingConfig) {
        val newBinding = activityProvider.bindToCurrentActivity { onOwningActivityDestroyed() }
            ?: return // no foreground activity to attach to

        activeManager = if (config.simpleLoading) simpleViewManager else advancedViewManager
        currentConfig = config
        binding = newBinding

        val attached = activeManager.attach(config, onDismiss = {
            config.onCancelled?.invoke()
            dismiss()
        })
        if (!attached) {
            clearBinding()
            return
        }

        val container = activeManager.container ?: return
        animator.scaleIn(container)
        config.onShown?.invoke()

        if (config.dismissOnBackPress) {
            backPressHandler.register { dismiss() }
        }
    }

    private fun onOwningActivityDestroyed() {
        clearBinding()
        backPressHandler.unregister()
        activeManager.release()
    }

    private fun clearBinding() {
        binding?.unbind()
        binding = null
    }
}