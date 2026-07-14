package com.unitx.signal_core.core

import android.app.Application
import com.unitx.signal_core.contract.config.SignalConfig
import com.unitx.signal_core.theme.ThemeResolver
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.LoadingHandler
import com.unitx.signal_core.handler.ToastHandler
import com.unitx.signal_core.helper.SignalAnimator
import com.unitx.signal_core.helper.SignalDismissScheduler
import com.unitx.signal_core.view.dialog.DialogViewManager
import com.unitx.signal_core.handler.SnackHandler
import com.unitx.signal_core.view.loading.LoadingViewManager
import com.unitx.signal_core.view.loading.SimpleLoadingViewManager
import com.unitx.signal_core.view.SnackViewManager
import com.unitx.signal_core.view.ToastViewManager
import com.unitx.signal_core.activity.ActivityProvider
import com.unitx.signal_core.handler.NotificationHandler
import com.unitx.signal_core.queue.QueueStrategy
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.view.NotificationViewManager

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
        viewManager = ToastViewManager(themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    internal val snackHandler = SnackHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.snackConfig,
        queue = getRequiredQueue(),
        viewManager = SnackViewManager(themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    internal val dialogHandler = DialogHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.dialogConfig,
        queue = getRequiredQueue(),
        viewManager = DialogViewManager(themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    internal val loadingHandler = LoadingHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.loadingConfig,
        advancedViewManager = LoadingViewManager(themeResolver),
        simpleViewManager = SimpleLoadingViewManager(themeResolver),
        animator = SignalAnimator
    )

    internal val notifHandler = NotificationHandler(
        activityProvider = activityProvider,
        globalConfig = globalConfig.notifConfig,
        queue = getRequiredQueue(),
        viewManager = NotificationViewManager(themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler()
    )

    private fun getRequiredQueue() = when (globalConfig.queueStrategy) {
        QueueStrategy.Independent -> SignalQueue()
        QueueStrategy.GlobalSequential -> sharedQueue
    }
}