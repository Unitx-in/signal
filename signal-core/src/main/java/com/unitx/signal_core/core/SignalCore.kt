package com.unitx.signal_core.core

import android.app.Application
import com.unitx.signal_core.common.config.SignalConfig
import com.unitx.signal_core.common.theme.SignalThemeResolver
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.ToastHandler
import com.unitx.signal_core.common.helper.SignalAnimator
import com.unitx.signal_core.common.helper.SignalDismissScheduler
import com.unitx.signal_core.view.DialogViewManager
import com.unitx.signal_core.handler.SnackHandler
import com.unitx.signal_core.view.SnackViewManager
import com.unitx.signal_core.view.ToastViewManager
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue

class SignalCore internal constructor(
    app: Application,
    config: SignalConfig
) {

    private val activityProvider = ActivityProvider(app)
    private val queue = SignalQueue()
    private val themeResolver = SignalThemeResolver(config.theme)

    internal val toastHandler = ToastHandler(
        activityProvider = activityProvider,
        globalConfig = config.toastConfig,
        queue = queue,
        viewManager = ToastViewManager(activityProvider, themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler
    )
    internal val snackHandler = SnackHandler(
        activityProvider = activityProvider,
        globalConfig = config.snackConfig,
        queue = queue,
        viewManager = SnackViewManager(activityProvider, themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler
    )
    internal val dialogHandler = DialogHandler(
        activityProvider = activityProvider,
        globalConfig = config.dialogConfig,
        queue = queue,
        viewManager = DialogViewManager(activityProvider, themeResolver),
        animator = SignalAnimator,
        scheduler = SignalDismissScheduler
    )
}