package com.unitx.signal_core.interop

/**
 * Java-friendly functional interface for lazily supplying a `Set<String>`.
 *
 * ```java
 * selectionConfig.setPreSelectedProvider(() -> currentSortOrders);
 * ```
 */
fun interface JavaStringSetSupplier {
    fun get(): Set<String>
}