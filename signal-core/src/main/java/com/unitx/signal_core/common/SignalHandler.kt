package com.unitx.signal_core.common

interface SignalHandler {
    fun show()
    fun dismiss()
    val isShowing: Boolean
}