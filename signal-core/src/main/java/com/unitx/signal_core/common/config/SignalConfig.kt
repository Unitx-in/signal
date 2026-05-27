package com.unitx.signal_core.common.config

import com.unitx.signal_core.common.config.base.BannerConfig
import com.unitx.signal_core.common.config.base.DialogConfig
import com.unitx.signal_core.common.config.base.LoadingConfig
import com.unitx.signal_core.common.config.base.SnackConfig
import com.unitx.signal_core.common.config.base.ToastConfig

class SignalConfig {
    internal val toastConfig = ToastConfig()
    internal val snackConfig = SnackConfig()
    internal val dialogConfig = DialogConfig()
    internal val bannerConfig = BannerConfig()
    internal val loadingConfig = LoadingConfig()

    fun toast(block: ToastConfig.() -> Unit) = toastConfig.apply(block)
    fun snack(block: SnackConfig.() -> Unit) = snackConfig.apply(block)
    fun dialog(block: DialogConfig.() -> Unit) = dialogConfig.apply(block)
    fun banner(block: BannerConfig.() -> Unit) = bannerConfig.apply(block)
    fun loading(block: LoadingConfig.() -> Unit) = loadingConfig.apply(block)
}