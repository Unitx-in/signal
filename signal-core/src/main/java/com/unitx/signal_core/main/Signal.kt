package com.unitx.signal_core.main

import android.app.Application
import com.unitx.signal_core.contract.config.base.DialogConfig
import com.unitx.signal_core.contract.config.SignalConfig
import com.unitx.signal_core.contract.config.base.SnackConfig
import com.unitx.signal_core.contract.config.base.ToastConfig
import com.unitx.signal_core.core.SignalCore

object Signal {

    private lateinit var core: SignalCore

    fun createCore(app: Application, block: SignalConfig.() -> Unit = {}) {
        val config = SignalConfig().apply(block)
        core = SignalCore(app, config)
    }

    fun toast(message: String) = core.toastHandler.show(message)
    fun toast(message: String, block: ToastConfig.() -> Unit) =
        core.toastHandler.show(message, block)

    fun snack(message: String) = core.snackHandler.show(message)
    fun snack(message: String, block: SnackConfig.() -> Unit){
        return core.snackHandler.show(message, block)
    }

    fun dialog(block: DialogConfig.() -> Unit) = core.dialogHandler.show(block)
}