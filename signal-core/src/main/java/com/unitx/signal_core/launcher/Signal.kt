package com.unitx.signal_core.launcher

import android.app.Application
import com.unitx.signal_core.common.SignalQueue
import com.unitx.signal_core.common.config.DialogConfig
import com.unitx.signal_core.common.config.SignalConfig
import com.unitx.signal_core.common.config.SnackConfig
import com.unitx.signal_core.common.config.ToastConfig
import com.unitx.signal_core.core.SignalCore
import com.unitx.signal_core.handler.DialogHandler
import com.unitx.signal_core.handler.SnackHandler
import com.unitx.signal_core.handler.ToastHandler

object Signal {

    private lateinit var config: SignalConfig
    private lateinit var host: SignalCore
    private lateinit var queue: SignalQueue

    private lateinit var toastHandler: ToastHandler
    private lateinit var snackHandler: SnackHandler
    private lateinit var dialogHandler: DialogHandler
//    private lateinit var bannerHandler: BannerHandler
//
//    val loading: LoadingHandler get() = LoadingHandler(
//        activity = host.current(),
//        defaultConfig = config.loadingConfig
//    )

    fun init(app: Application, block: SignalConfig.() -> Unit = {}) {
        config = SignalConfig().apply(block)
        host = SignalCore(app)
        queue = SignalQueue()

        toastHandler = ToastHandler(host, config.toastConfig)
        snackHandler = SnackHandler(host, config.snackConfig, queue)
        dialogHandler = DialogHandler(host, config.dialogConfig)
//        bannerHandler = BannerHandler(host, config.bannerConfig, queue)
    }

    // --- Toast ---
    fun toast(message: String) = toastHandler.show(message)
    fun toast(message: String, block: ToastConfig.() -> Unit) =
        toastHandler.show(message, block)

    // --- Snack ---
    fun snack(message: String) = snackHandler.show(message)
    fun snack(message: String, block: SnackConfig.() -> Unit) =
        snackHandler.show(message, block)

    // --- Dialog ---
    fun dialog(block: DialogConfig.() -> Unit) = dialogHandler.show(block)

//    // --- Banner ---
//    fun banner(message: String) = bannerHandler.show(message)
//    fun banner(message: String, block: BannerConfig.() -> Unit) =
//        bannerHandler.show(message, block)
}