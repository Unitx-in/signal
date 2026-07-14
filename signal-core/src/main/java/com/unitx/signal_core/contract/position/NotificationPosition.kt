package com.unitx.signal_core.contract.position

/**
 * Screen position where the notification appears.
 * Configured via [NotificationConfig.position].
 */
enum class NotificationPosition {
    /** Notification appears near the top of the screen. */
    Top,
    /** Notification appears in the center of the screen. */
    Center,
    /** Notification appears near the bottom of the screen. */
    Bottom
}