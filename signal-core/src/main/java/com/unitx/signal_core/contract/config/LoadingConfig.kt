package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.model.LoadingAnimationAttr
import com.unitx.signal_core.contract.type.LoadingType

class LoadingConfig {
    var title: String = "Please wait a moment."
    var subtitle: String? = null

    // Progress
    var type: LoadingType = LoadingType.Indefinite
    var progress: Int = 0                          // 0-100, used when type = Determinate
    var progressMessage: String? = null            // appended to progress: "10% · Uploading files"

    // Icon
    @DrawableRes var icon: Int? = null             // shown in center of lottie ring

    var horizontalMargin: Int = 12
    var cancelable: Boolean = false
    var dismissOnBackPress: Boolean = false
    var onShown: (() -> Unit)? = null
    var onDismissed: (() -> Unit)? = null
    var onCancelled: (() -> Unit)? = null
    var accessibilityText: String? = null

    var animationAttr: LoadingAnimationAttr = LoadingAnimationAttr()

    internal fun copy(): LoadingConfig = LoadingConfig().also {
        it.title = title
        it.subtitle = subtitle
        it.type = type
        it.progress = progress
        it.progressMessage = progressMessage
        it.icon = icon
        it.cancelable = cancelable
        it.dismissOnBackPress = dismissOnBackPress
        it.onShown = onShown
        it.onDismissed = onDismissed
        it.onCancelled = onCancelled
        it.horizontalMargin = horizontalMargin
        it.animationAttr = animationAttr
        it.accessibilityText = accessibilityText
    }
}