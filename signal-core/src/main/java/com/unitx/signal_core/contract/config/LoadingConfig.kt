package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.model.LoadingAnimationAttr
import com.unitx.signal_core.contract.type.LoadingType
import com.unitx.signal_core.interop.JavaVoidCallback

/**
 * Configuration for a loading overlay signal.
 *
 * Usage:
 * ```
 * Signal.loading(this) {
 *     title = "Uploading..."
 *     type = LoadingType.Determinate
 *     cancelable = true
 *     onCancelled = { cancelUpload() }
 * }
 *
 * // Simple dots-only overlay:
 * Signal.loading(this) { simpleLoading = true }
 * ```
 */
class LoadingConfig {

    /** Primary text shown below the animation. Ignored when [simpleLoading] is true. Default: "Please wait a moment." */
    var title: String = "Please wait a moment."

    /** Secondary text shown below the title. Ignored when [simpleLoading] is true. */
    var subtitle: String? = null

    /** If true, shows a minimal dots-only overlay with no text or icon. Default: false. */
    var simpleLoading: Boolean = false

    /** Loading style. Use [LoadingType.Determinate] to show progress percentage. Default: [LoadingType.Indefinite]. */
    var type: LoadingType = LoadingType.Indefinite

    /** Current progress (0–100). Only used when [type] is [LoadingType.Determinate]. */
    var progress: Int = 0

    /** Optional message appended to the progress text, e.g. "Uploading files" → "10% · Uploading files". */
    var progressMessage: String? = null

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
     *
     * Called when the overlay becomes visible.
     */
    fun onShown(block: JavaVoidCallback) {
        onShown = { block.invoke() }
    }

    /** Called when the overlay is dismissed for any reason. */
    var onDismissed: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onDismissed]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called when the overlay is dismissed for any reason.
     */
    fun onDismissed(block: JavaVoidCallback) {
        onDismissed = { block.invoke() }
    }

    /** Called when the user cancels via tap or back press. */
    var onCancelled: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onCancelled]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called when the user cancels via tap or back press.
     */
    fun onCancelled(block: JavaVoidCallback) {
        onCancelled = { block.invoke() }
    }

    /** Overrides the default accessibility description. */
    var accessibilityText: String? = null

    /** Fine-grained control over the dots animation timing and appearance. */
    var animationAttr: LoadingAnimationAttr = LoadingAnimationAttr()

    internal fun copy(): LoadingConfig = LoadingConfig().also {
        it.title = title
        it.subtitle = subtitle
        it.type = type
        it.progress = progress
        it.progressMessage = progressMessage
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