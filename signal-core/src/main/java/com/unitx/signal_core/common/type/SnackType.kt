package com.unitx.signal_core.common.type

import com.unitx.signal_core.R


enum class SnackType(val icon: Int, val backColorLight: Int, val backColorDark: Int) {
    Error(R.drawable.ic_snack_error, R.color.signalLightRed, R.color.signalRed),
    Warning(R.drawable.ic_snack_warning, R.color.signalLightYellow, R.color.signalYellow),
    Info(R.drawable.ic_snack_info, R.color.signalLightBlue, R.color.signalBlue),
    Success(R.drawable.ic_snack_success, R.color.signalLightGreen, R.color.signalGreen)
    ;

    companion object {
        val default = Info
    }
}