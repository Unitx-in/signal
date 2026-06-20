package com.unitx.signal_core.contract.model

internal data class SignalAction(
    val label: String,
    val onClick: () -> Unit
)
