package com.unitx.signal_core.contract.config.dialog

class DialogScope {
    internal var shouldDismiss: Boolean = true

    fun dismiss() { shouldDismiss = true }
    fun prevent() { shouldDismiss = false }
}