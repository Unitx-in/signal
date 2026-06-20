package com.unitx.signal_core.core

import android.app.Application
import com.unitx.signal_core.contract.config.SignalConfig
import com.unitx.signal_core.theme.ThemeResolver
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.LoadingHandler
import com.unitx.signal_core.handler.ToastHandler
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
import com.unitx.signal_core.view.DialogViewManager
import com.unitx.signal_core.handler.SnackHandler
import com.unitx.signal_core.view.loading.LoadingViewManager
import com.unitx.signal_core.view.loading.SimpleLoadingViewManager
import com.unitx.signal_core.view.SnackViewManager
import com.unitx.signal_core.view.ToastViewManager
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.QueueStrategy
import com.unitx.signal_core.queue.SignalQueue

internal class SignalCore(
    app: Application, private val globalConfig: SignalConfig
) {

    private val activityProvider = ActivityProvider(app)
    private val sharedQueue = SignalQueue()
    private val themeResolver = ThemeResolver(globalConfig.theme)

    internal val toastHandler = ToastHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.toastConfig,
        queue = getRequiredQueue(),
        viewManager = ToastViewManager(activityProvider, themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    internal val snackHandler = SnackHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.snackConfig,
        queue = getRequiredQueue(),
        viewManager = SnackViewManager(activityProvider, themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    internal val dialogHandler = DialogHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.dialogConfig,
        queue = getRequiredQueue(),
        viewManager = DialogViewManager(activityProvider, themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    internal val loadingHandler = LoadingHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.loadingConfig,
        advancedViewManager = LoadingViewManager(activityProvider, themeResolver),
        simpleViewManager = SimpleLoadingViewManager(activityProvider, themeResolver),
        animator = SignalAnimator
    )

    private fun getRequiredQueue() = when (globalConfig.queueStrategy) {
        QueueStrategy.Independent -> SignalQueue()
        QueueStrategy.GlobalSequential -> sharedQueue
    }
}