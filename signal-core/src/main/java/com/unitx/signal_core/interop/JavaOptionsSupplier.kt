package com.unitx.signal_core.interop

import com.unitx.signal_core.contract.model.DialogSelectionOption

/**
 * Java-friendly functional interface for lazily supplying a list of
 * [DialogSelectionOption].
 *
 * ```java
 * dropdownConfig.setOptionsProvider(() -> fetchSortOptions());
 * ```
 */
fun interface JavaOptionsSupplier {
    fun get(): List<DialogSelectionOption>
}