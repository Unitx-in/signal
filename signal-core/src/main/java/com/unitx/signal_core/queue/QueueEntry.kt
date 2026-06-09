package com.unitx.signal_core.queue

data class QueueEntry(
    val show: () -> Unit,
    val dismiss: () -> Unit,
    val isShowing: () -> Boolean
)