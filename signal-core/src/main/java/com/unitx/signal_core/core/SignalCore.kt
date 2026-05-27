package com.unitx.signal_core.core

import android.app.Application
import com.unitx.signal_core.queue.SignalQueue
import com.unitx.signal_core.common.config.SignalConfig
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.snack.SnackHandler
import com.unitx.signal_core.handler.ToastHandler
import com.unitx.signal_core.handler.snack.SnackAnimator
import com.unitx.signal_core.handler.snack.SnackDismissScheduler
import com.unitx.signal_core.handler.snack.SnackViewManager
import com.unitx.signal_core.provider.ActivityProvider

class SignalCore(
    app: Application,
    config: SignalConfig
) {

    internal val activityProvider = ActivityProvider(app)
    private val queue = SignalQueue()

    internal val toastHandler = ToastHandler(activityProvider, config.toastConfig)
    internal val snackHandler = SnackHandler(
        activityProvider = activityProvider,
        defaultConfig = config.snackConfig,
        queue = queue,
        viewManager = SnackViewManager(activityProvider),
        animator = SnackAnimator(),
        scheduler = SnackDismissScheduler()
    )
    internal val dialogHandler = DialogHandler(activityProvider, config.dialogConfig)
}