package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.position.NotificationPosition
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.interop.JavaVoidCallback

/**
 * Configuration for a notification signal (Pinterest-style "Saved to X" toast).
 *
 * Usage:
 * ```
 * Signal.notif(this) {
 *     message = "Saved to"
 *     highlight = "Men fashion casual outfits"
 *     iconRes = R.drawable.ic_board_thumb
 * }
 * ```
 */
class NotificationConfig {

    /** Prevents duplicate notifications with the same tag from queuing. */
    var tag: String? = null

    /** The leading, regular-weight text (e.g. "Saved to"). */
    var message: String = ""

    /** The trailing, bold-weight text (e.g. "Men fashion casual outfits"). */
    var highlight: String = ""

    /** Display duration in ms. Default: 2500. */
    var duration: Long = 2500L

    /** Screen position of the notification. Default: [ToastPosition.Bottom]. */
    var position: NotificationPosition = NotificationPosition.Top

    /** Icon shown in the leading [ShapeableImageView] slot (e.g. board thumbnail, avatar). */
    @DrawableRes var iconRes: Int? = null

    /** Remote image URL for the icon slot, loaded in place of [iconRes] if set. */
    var iconUrl: String? = null

    /** If true, tapping the notification dismisses it immediately. Default: true. */
    var dismissOnTap: Boolean = true

    /** Additional offset from the top edge in px. Applied when [position] is Top. */
    var topOffset: Int = 0

    /** Additional offset from the bottom edge in px. Applied when [position] is Bottom. */
    var bottomOffset: Int = 0

    /** Called when the notification becomes visible. */
    var onShown: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onShown]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called when the notification becomes visible.
     */
    fun onShown(block: JavaVoidCallback) {
        onShown = { block.invoke() }
    }

    /** Called when the notification is dismissed. */
    var onDismissed: (() -> Unit)? = null

    /**
     * Java-friendly setter for [onDismissed]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called when the notification is dismissed.
     */
    fun onDismissed(block: JavaVoidCallback) {
        onDismissed = { block.invoke() }
    }

    /** Overrides the default accessibility description. */
    var accessibilityText: String? = null

    /** If true, back press dismisses the notification. Default: false. */
    var dismissOnBackPress: Boolean = false

    internal fun copy(): NotificationConfig = NotificationConfig().also {
        it.tag = tag
        it.message = message
        it.highlight = highlight
        it.duration = duration
        it.position = position
        it.iconRes = iconRes
        it.iconUrl = iconUrl
        it.dismissOnTap = dismissOnTap
        it.topOffset = topOffset
        it.bottomOffset = bottomOffset
        it.onShown = onShown
        it.onDismissed = onDismissed
        it.accessibilityText = accessibilityText
        it.dismissOnBackPress = dismissOnBackPress
    }
}