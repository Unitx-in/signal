package com.unitx.signal_core.contract.model

data class SignalAction(
    val label: String,
    val onClick: () -> Unit
)
