package com.unitx.signal_core.contract.type

import androidx.annotation.ColorRes
import com.unitx.signal_core.R

enum class ToastType(@ColorRes val backgroundColor: Int, @ColorRes val foregroundColor: Int) {
    Error(R.color.signalLightRed, R.color.signalRed),
    Warning(R.color.signalLightYellow, R.color.signalYellow),
    Info(R.color.signalLightBlue, R.color.signalBlue),
    Success(R.color.signalLightGreen, R.color.signalGreen)
}