package com.unitx.signal_core.interop

fun interface JavaUnitCallback<T> {
    fun invoke(value: T)
}