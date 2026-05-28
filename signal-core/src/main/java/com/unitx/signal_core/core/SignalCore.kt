package com.unitx.signal_core.core

import android.app.Application
import com.unitx.signal_core.common.config.SignalConfig
import com.unitx.signal_core.common.theme.SignalThemeResolver
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.toast.ToastHandler
import com.unitx.signal_core.common.helper.SignalAnimator
import com.unitx.signal_core.common.helper.SignalDismissScheduler
import com.unitx.signal_core.handler.snackbar.SnackHandler
import com.unitx.signal_core.handler.snackbar.SnackViewManager
import com.unitx.signal_core.handler.toast.ToastViewManager
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue

class SignalCore(
    app: Application,
    config: SignalConfig
) {

    internal val activityProvider = ActivityProvider(app)
    private val queue = SignalQueue()
    private val themeResolver = SignalThemeResolver(config.theme)

    internal val toastHandler = ToastHandler(
        activityProvider = activityProvider,
        globalConfig = config.toastConfig,
        queue = queue,
        viewManager = ToastViewManager(activityProvider, themeResolver),
        animator = SignalAnimator(),
        scheduler = SignalDismissScheduler()
    )
    internal val snackHandler = SnackHandler(
        activityProvider = activityProvider,
        globalConfig = config.snackConfig,
        queue = queue,
        viewManager = SnackViewManager(activityProvider, themeResolver),
        animator = SignalAnimator(),
        scheduler = SignalDismissScheduler()
    )
    internal val dialogHandler = DialogHandler(activityProvider, config.dialogConfig)
}