package com.unitx.signal_core.interop

/**
 * Java-friendly functional interface for lazily supplying an [Int].
 *
 * ```java
 * loadingConfig.setProgressProvider(() -> uploadTask.getPercentComplete());
 * ```
 */
fun interface JavaIntSupplier {
    fun get(): Int
}