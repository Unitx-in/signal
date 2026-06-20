package com.unitx.signal_core.contract.type

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.unitx.signal_core.R

/**
 * Visual style of a snackbar — controls the icon and its background tint.
 * Configured via [SnackConfig.type].
 */
enum class SnackType(
    @DrawableRes val icon: Int, @ColorRes val iconBackLight: Int, @ColorRes val iconBackDark: Int
) {
    /** Red icon tint. Use for errors or failures. */
    Error(R.drawable.ic_signal_error, R.color.signalLightRed, R.color.signalRed),

    /** Yellow icon tint. Use for warnings or caution messages. */
    Warning(R.drawable.ic_signal_warning, R.color.signalLightYellow, R.color.signalYellow),

    /** Blue icon tint. Use for neutral informational messages. */
    Info(R.drawable.ic_signal_info, R.color.signalLightBlue, R.color.signalBlue),

    /** Green icon tint. Use for success or confirmation messages. */
    Success(R.drawable.ic_signal_success, R.color.signalLightGreen, R.color.signalGreen);
}