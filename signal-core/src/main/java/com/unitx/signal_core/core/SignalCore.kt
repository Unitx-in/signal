package com.unitx.signal_core.core

import android.app.Application
import com.unitx.signal_core.common.config.SignalConfig
import com.unitx.signal_core.common.theme.SignalThemeResolver
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.ToastHandler
import com.unitx.signal_core.handler.snack.SnackAnimator
import com.unitx.signal_core.handler.snack.SnackDismissScheduler
import com.unitx.signal_core.handler.snack.SnackHandler
import com.unitx.signal_core.handler.snack.SnackViewManager
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.queue.SignalQueue

class SignalCore(
    app: Application,
    config: SignalConfig
) {

    internal val activityProvider = ActivityProvider(app)
    private val queue = SignalQueue()
    private val themeResolver = SignalThemeResolver(config.theme)

    internal val toastHandler = ToastHandler(activityProvider, config.toastConfig)
    internal val snackHandler = SnackHandler(
        activityProvider = activityProvider,
        globalConfig = config.snackConfig,
        queue = queue,
        viewManager = SnackViewManager(activityProvider, themeResolver),
        animator = SnackAnimator(),
        scheduler = SnackDismissScheduler()
    )
    internal val dialogHandler = DialogHandler(activityProvider, config.dialogConfig)
}