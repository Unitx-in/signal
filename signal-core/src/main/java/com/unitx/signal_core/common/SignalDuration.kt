package com.unitx.signal_core.common

sealed class SignalDuration {
    object Short : SignalDuration()
    object Long : SignalDuration()
    object Indefinite : SignalDuration()
}