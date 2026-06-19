package com.unitx.signal_core.view.loading

import android.view.View
import com.unitx.signal_core.contract.config.LoadingConfig

internal interface ILoadingViewManager {
    val container: View?
    val isShowing: Boolean
    fun attach(config: LoadingConfig, onDismiss: () -> Unit): Boolean
    fun release(onReleased: () -> Unit = {})
    fun updateProgress(config: LoadingConfig) {}
}