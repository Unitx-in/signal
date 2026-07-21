package com.unitx.signal_core.contract.config

import com.unitx.signal_core.contract.model.SignalAction
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType
import com.unitx.signal_core.interop.JavaVoidCallback

/**
 * Configuration for a snackbar signal.
 *
 * Usage:
 * ```
 * Signal.snack(this, "Changes saved") {
 *     type = SnackType.Success
 *     action("Undo") { undoChanges() }
 *     persistent = true
 * }
 * ```
 */
class SnackConfig {

    /** Prevents duplicate snackbars with the same tag from queuing. */
    var tag: String? = null

    /** The message to display. Set automatically when calling [Signal.snack]. */
    var message: String = ""

    /** Display duration in ms. Ignored when [persistent] is true. Default: 2500. */
    var duration: Long = 2500L

    /** If true, back press dismisses the snackbar. Default: false. */
    var dismissOnBackPress: Boolean = false

    /** Additional offset from the top edge in px. Applied when [position] is Top. */
    var topOffset: Int = 0

    /** Additional offset from the bottom edge in px. Applied when [position] is Bottom. */
    var bottomOffset: Int = 0

    /** Screen position of the snackbar. Default: [SnackPosition.Bottom]. */
    var position: SnackPosition = SnackPosition.Bottom

    /** Visual style — affects icon and background. Default: [SnackType.Info]. */
    var type: SnackType = SnackType.Info

    /** If true, shows the cancel (✕) button. Default: true. */
    var showCancelAction: Boolean = true

    /** If true, the snackbar stays until explicitly dismissed. Also forces the cancel button visible. */
    var persistent: Boolean = false

    /** Called when the snackbar becomes visible. */
    var onShown: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onShown]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called when the snackbar becomes visible.
     */
    fun onShown(block: JavaVoidCallback) {
        onShown = { block.invoke() }
    }

    /** Called when the snackbar is dismissed. */
    var onDismissed: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onDismissed]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called when the snackbar is dismissed.
     */
    fun onDismissed(block: JavaVoidCallback) {
        onDismissed = { block.invoke() }
    }

    /** Overrides the default accessibility description. */
    var accessibilityText: String? = null

    /** Adds a text action button on the right side. */
    fun action(label: String, onClick: () -> Unit) {
        action = SignalAction(label = label, onClick = onClick)
    }

    /**
     * Java-friendly overload of [action]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Adds a text action button on the right side.
     */
    fun action(label: String, onClick: JavaVoidCallback) {
        action = SignalAction(label = label, onClick = { onClick.invoke() })
    }

    internal var action: SignalAction? = null

    internal fun copy(): SnackConfig = SnackConfig().also {
        it.message = message
        it.duration = duration
        it.dismissOnBackPress = dismissOnBackPress
        it.topOffset = topOffset
        it.bottomOffset = bottomOffset
        it.position = position
        it.type = type
        it.showCancelAction = showCancelAction
        it.action = action
        it.tag = tag
        it.onShown = onShown
        it.onDismissed = onDismissed
        it.accessibilityText = accessibilityText
        it.persistent = persistent
    }
}