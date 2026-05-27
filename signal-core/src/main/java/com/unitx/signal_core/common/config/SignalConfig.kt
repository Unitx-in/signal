package com.unitx.signal_core.common.config

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