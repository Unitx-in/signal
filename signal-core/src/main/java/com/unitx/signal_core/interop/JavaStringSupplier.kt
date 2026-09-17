package com.unitx.signal_core.interop

/**
 * Java-friendly functional interface for lazily supplying a [String].
 *
 * ```java
 * dialogInputConfig.setPrefillProvider(() -> currentName);
 * ```
 */
fun interface JavaStringSupplier {
    fun get(): String
}