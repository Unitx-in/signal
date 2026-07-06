package com.unitx.signal_core.contract.config

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.contract.type.ToastType

/**
 * Configuration for a toast signal.
 *
 * Usage:
 * ```
 * Signal.toast(this, "File saved") {
 *     type = ToastType.Success
 *     position = ToastPosition.Top
 *     duration = 3000L
 * }
 * ```
 */
class ToastConfig {

    /** Prevents duplicate toasts with the same tag from queuing. */
    var tag: String? = null

    /** The message to display. Set automatically when calling [Signal.toast]. */
    var message: String = ""

    /** Display duration in ms. Default: 2000. */
    var duration: Long = 2000L

    /** Screen position of the toast. Default: [ToastPosition.Bottom]. */
    var position: ToastPosition = ToastPosition.Bottom

    /** Visual style — affects background and icon color. Default: [ToastType.Info]. */
    var type: ToastType = ToastType.Info

    /** If true, tapping the toast dismisses it immediately. Default: true. */
    var dismissOnTap: Boolean = true

    /** Additional offset from the top edge in px. Applied when [position] is Top. */
    var topOffset: Int = 0

    /** Additional offset from the bottom edge in px. Applied when [position] is Bottom. */
    var bottomOffset: Int = 0

    /** Optional icon drawn alongside the message. */
    @DrawableRes var iconRes: Int? = null

    /** Position of [iconRes] relative to the message text. Default: [IconPosition.Start]. */
    var iconPosition: IconPosition = IconPosition.Start

    /** Called when the toast becomes visible. */
    var onShown: (() -> Unit)? = null

    /** Called when the toast is dismissed. */
    var onDismissed: (() -> Unit)? = null

    /** Overrides the default accessibility description. */
    var accessibilityText: String? = null

    internal fun copy(): ToastConfig = ToastConfig().also {
        it.duration = duration
        it.position = position
        it.type = type
        it.iconRes = iconRes
        it.iconPosition = iconPosition
        it.dismissOnTap = dismissOnTap
        it.tag = tag
        it.topOffset = topOffset
        it.bottomOffset = bottomOffset
        it.onShown = onShown
        it.onDismissed = onDismissed
        it.accessibilityText = accessibilityText
        it.message = message
    }
}