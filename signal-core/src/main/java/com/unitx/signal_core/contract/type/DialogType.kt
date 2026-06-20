package com.unitx.signal_core.contract.type

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unitx.signal_core.R

/**
 * Visual style of a dialog — controls the header color, icon, and default header label.
 * Configured via [DialogConfig.type].
 */
enum class DialogType(
    @DrawableRes val icon: Int,
    @ColorRes val secondaryColor: Int,
    @ColorRes val primaryColor: Int,
    @StringRes val header: Int,
) {
    /** Red theme. Use for destructive actions or critical errors. */
    Error(
        R.drawable.ic_signal_error,
        R.color.signalLightRed,
        R.color.signalRed,
        R.string.dialog_error_header
    ),
    /** Yellow theme. Use for confirmations that require user attention. */
    Action(
        R.drawable.ic_signal_warning,
        R.color.signalLightYellow,
        R.color.signalYellow,
        R.string.dialog_action_header
    ),
    /** Blue theme. Use for general informational dialogs. */
    Default(
        R.drawable.ic_signal_info,
        R.color.signalLightBlue,
        R.color.signalBlue,
        R.string.dialog_default_header
    ),
    /** Green theme. Use for success confirmations or positive outcomes. */
    Positive(
        R.drawable.ic_signal_success,
        R.color.signalLightGreen,
        R.color.signalGreen,
        R.string.dialog_positive_header
    );
}