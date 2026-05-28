package com.unitx.signal_core.common.type

import com.unitx.signal_core.R

enum class ToastType(val strokeColorLight: Int, val strokeColorDark: Int) {
    Error(R.color.signalLightRed, R.color.signalRed),
    Warning(R.color.signalLightYellow, R.color.signalYellow),
    Info(R.color.signalLightBlue, R.color.signalBlue),
    Success(R.color.signalLightGreen, R.color.signalGreen)
}