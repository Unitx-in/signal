package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.model.LoadingAnimationAttr
import com.unitx.signal_core.contract.type.LoadingType
import com.unitx.signal_core.interop.JavaIntSupplier
import com.unitx.signal_core.interop.JavaVoidCallback

/**
 * Configuration for a loading overlay signal.
 *
 * [progressProvider] / [progressMessageProvider] are evaluated lazily on
 * each render tick while the overlay is visible, so the displayed progress
 * can keep advancing after the overlay is shown.
 *
 * Usage:
 * ```kotlin
 * Signal.loading(this) {
 *     title = "Uploading..."
 *     type = LoadingType.Determinate
 *     progressProvider = { uploadTask.percentComplete }
 *     cancelable = true
 *     onCancelled = { cancelUpload() }
 * }
 *
 * // Simple dots-only overlay:
 * Signal.loading(this) { simpleLoading = true }
 * ```
 */
class LoadingConfig {

    /** Current progress (0–100), resolved from [progressProvider] on each render tick. 0 if unset. */
    internal val progress: Int get() = progressProvider?.invoke() ?: 0

    /** Progress message, resolved from [progressMessageProvider] on each render tick. Null if unset. */
    internal val progressMessage: String? get() = progressMessageProvider?.invoke()

    /** Primary text shown below the animation. Ignored when [simpleLoading] is true. Default: "Please wait a moment." */
    var title: String = "Please wait a moment."

    /** Secondary text shown below the title. Ignored when [simpleLoading] is true. */
    var subtitle: String? = null

    /** If true, shows a minimal dots-only overlay with no text or icon. Default: false. */
    var simpleLoading: Boolean = false

    /** Loading style. Use [LoadingType.Determinate] to show progress percentage. Default: [LoadingType.Indefinite]. */
    var type: LoadingType = LoadingType.Indefinite

    /** Supplies current progress (0–100) on each render tick. Only used when [type] is [LoadingType.Determinate]. */
    var progressProvider: (() -> Int)? = null
        @JvmName("setProgressProviderKt") set

    /** Supplies the progress message on each render tick, e.g. "Uploading files" → "10% · Uploading files". */
    var progressMessageProvider: (() -> String?)? = null
        @JvmName("setProgressMessageProviderKt") set

    /** Optional icon shown. Ignored when [simpleLoading] is true. */
    @DrawableRes var icon: Int? = null

    /** Optional remote icon URL. Takes precedence over [icon] if both are set. Ignored when [simpleLoading] is true. */
    var iconUrl: String? = null

    /** Disables loading icon color as icon url can fetch images. */
    var disableIconColor: Boolean = false

    /** Horizontal margin from screen edges in dp. Ignored when [simpleLoading] is true. Default: 12. */
    var horizontalMargin: Int = 12

    /** If true, tapping the dim overlay dismisses the loading and triggers [onCancelled]. Default: false. */
    var cancelable: Boolean = false

    /** If true, back press dismisses the loading overlay. Default: false. */
    var dismissOnBackPress: Boolean = false

    /** Called when the overlay becomes visible. */
    var onShown: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onShown]. Avoids requiring `return null;`
     * from Java lambdas.
     */
    fun onShown(block: JavaVoidCallback) {
        onShown = { block.invoke() }
    }

    /** Called when the overlay is dismissed for any reason. */
    var onDismissed: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onDismissed]. Avoids requiring `return null;`
     * from Java lambdas.
     */
    fun onDismissed(block: JavaVoidCallback) {
        onDismissed = { block.invoke() }
    }

    /** Called when the user cancels via tap or back press. */
    var onCancelled: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onCancelled]. Avoids requiring `return null;`
     * from Java lambdas.
     */
    fun onCancelled(block: JavaVoidCallback) {
        onCancelled = { block.invoke() }
    }

    /** Overrides the default accessibility description. */
    var accessibilityText: String? = null

    /** Fine-grained control over the dots animation timing and appearance. */
    var animationAttr: LoadingAnimationAttr = LoadingAnimationAttr()

    /**
     * Java-friendly setter for [progressProvider].
     * Avoids requiring Java callers to implement a Kotlin `Function0<Integer>`.
     */
    fun setProgressProvider(supplier: JavaIntSupplier?) {
        progressProvider = supplier?.let { { it.get() } }
    }

    internal fun copy(): LoadingConfig = LoadingConfig().also {
        it.title = title
        it.subtitle = subtitle
        it.type = type
        it.progressProvider = progressProvider
        it.progressMessageProvider = progressMessageProvider
        it.icon = icon
        it.iconUrl = iconUrl
        it.cancelable = cancelable
        it.dismissOnBackPress = dismissOnBackPress
        it.onShown = onShown
        it.onDismissed = onDismissed
        it.onCancelled = onCancelled
        it.horizontalMargin = horizontalMargin
        it.animationAttr = animationAttr
        it.accessibilityText = accessibilityText
        it.simpleLoading = simpleLoading
        it.disableIconColor = disableIconColor
    }
}