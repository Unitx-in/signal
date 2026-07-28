package com.unitx.signal_core.contract.model

internal data class FieldBinding(
    val commit: () -> Unit,
    val validate: () -> Boolean
)