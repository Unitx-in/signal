package com.unitx.signal_core.common.type

import androidx.annotation.ColorRes
import com.unitx.signal_core.R

enum class ToastType(@ColorRes val strokeColorLight: Int, @ColorRes val strokeColorDark: Int) {
    Error(R.color.toastLightRed, R.color.signalRed),
    Warning(R.color.signalLightYellow, R.color.signalYellow),
    Info(R.color.signalLightBlue, R.color.signalBlue),
    Success(R.color.signalLightGreen, R.color.signalGreen)
}