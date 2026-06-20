package com.unitx.signal_core.contract.position

/**
 * Position of the icon relative to the message text in a toast.
 * Configured via [ToastConfig.iconPosition].
 */
enum class IconPosition {
    /** Icon appears to the left of the message. */
    Start,
    /** Icon appears to the right of the message. */
    End,
    /** Icon appears above the message. */
    Top,
    /** Icon appears below the message. */
    Bottom
}