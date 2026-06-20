package com.unitx.signal_core.contract.type

import androidx.annotation.ColorRes
import com.unitx.signal_core.R

/**
 * Visual style of a toast — controls the background and foreground colors.
 * Configured via [ToastConfig.type].
 */
enum class ToastType(@ColorRes val backgroundColor: Int, @ColorRes val foregroundColor: Int) {
    /** Light red background. Use for errors or failures. */
    Error(R.color.signalLightRed, R.color.signalRed),
    /** Light yellow background. Use for warnings or caution messages. */
    Warning(R.color.signalLightYellow, R.color.signalYellow),
    /** Light blue background. Use for neutral informational messages. */
    Info(R.color.signalLightBlue, R.color.signalBlue),
    /** Light green background. Use for success or confirmation messages. */
    Success(R.color.signalLightGreen, R.color.signalGreen)
}