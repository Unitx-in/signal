package com.unitx.signal_core.common.type

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.unitx.signal_core.R


enum class DialogType(
    @DrawableRes val icon: Int,
    @ColorRes val secondaryColor: Int,
    @ColorRes val primaryColor: Int,
    @StringRes val header: Int,

    ) {
    Error(
        R.drawable.ic_signal_error,
        R.color.signalLightRed,
        R.color.signalRed,
        R.string.dialog_error_header
    ),
    Action(
        R.drawable.ic_signal_warning,
        R.color.signalLightYellow,
        R.color.signalYellow,
        R.string.dialog_action_header
    ),
    Default(
        R.drawable.ic_signal_info,
        R.color.signalLightBlue,
        R.color.signalBlue,
        R.string.dialog_default_header
    ),
    Positive(
        R.drawable.ic_signal_success,
        R.color.signalLightGreen,
        R.color.signalGreen,
        R.string.dialog_positive_header
    );
}