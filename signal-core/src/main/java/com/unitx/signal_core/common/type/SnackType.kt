package com.unitx.signal_core.common.type

import com.unitx.signal_core.R


enum class SnackType(val icon: Int, val backColor: Int) {
    Error(R.drawable.ic_snack_error, R.color.signalLightRed),
    Warning(R.drawable.ic_snack_warning, R.color.signalLightYellow),
    Info(R.drawable.ic_snack_info, R.color.signalLightBlue),
    Success(R.drawable.ic_snack_success, R.color.signalLightGreen)
    ;
}