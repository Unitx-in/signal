package com.unitx.signal_core.contract.config.dialog

import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.contract.type.DialogSelectionMode
import com.unitx.signal_core.interop.JavaOptionsSupplier
import com.unitx.signal_core.interop.JavaStringSetSupplier
import com.unitx.signal_core.interop.JavaUnitCallback

/**
 * Optional selection list configuration for a dialog — renders as radio buttons,
 * checkboxes, or chips depending on [mode].
 *
 * [optionsProvider] and [preSelectedProvider] are evaluated lazily at show
 * time rather than when this config block runs, so they reflect current
 * data even if the dialog sits in a queue behind other signals before it's
 * actually shown.
 *
 * Usage:
 * ```kotlin
 * Signal.dialog(this) {
 *     title = "Sort by"
 *     selection {
 *         mode = DialogSelectionType.SINGLE
 *         optionsProvider = { fetchSortOptions() }
 *         preSelectedProvider = { currentSortOrders }
 *         onSelected = { selected -> }
 *     }
 *     positive("Apply") {}
 * }
 * ```
 */
class DialogSelectionConfig {

    /** Options to display, resolved from [optionsProvider] at show time. Empty if unset. */
    internal val options: List<DialogSelectionOption> get() = optionsProvider?.invoke() ?: emptyList()

    /** Selected values, resolved from [preSelectedProvider] at show time. Empty if unset. */
    internal val preSelected: Set<String> get() = preSelectedProvider?.invoke() ?: emptySet()

    /** Optional heading shown above this selection group. Recommended when stacking multiple selection groups in one dialog. */
    var label: String = ""

    /** Selection UI style: SINGLE (radio), MULTI (checkbox), or CHIP. */
    var mode: DialogSelectionMode = DialogSelectionMode.SINGLE

    /** Supplies the options list, evaluated lazily at show time. */
    var optionsProvider: (() -> List<DialogSelectionOption>)? = null
        @JvmName("setOptionsProviderKt") set

    /** Supplies the default-selected values, evaluated lazily at show time. */
    var preSelectedProvider: (() -> Set<String>)? = null
        @JvmName("setPreSelectedProviderKt") set

    /** Called with the final selected values when the positive button is tapped. */
    var onSelected: ((Set<String>) -> Unit)? = null

    /**
     * Java-friendly setter for [onSelected]. Avoids requiring `return null;`
     * from Java lambdas.
     */
    fun onSelected(block: JavaUnitCallback<Set<String>>) {
        onSelected = { block.invoke(it) }
    }

    /** Convenience — builds options from plain string labels. */
    fun options(vararg labels: String) {
        optionsProvider = { labels.map { DialogSelectionOption(it) } }
    }

    /** If provided, submit is blocked until this returns true for the current selection. */
    var validator: ((Set<String>) -> Boolean)? = null

    /** Error message shown below the group when [validator] returns false. */
    var validationError: String = ""

    /**
     * Java-friendly setter for [optionsProvider].
     * Avoids requiring Java callers to implement a Kotlin
     * `Function0<List<DialogSelectionOption>>`.
     */
    fun setOptionsProvider(supplier: JavaOptionsSupplier?) {
        optionsProvider = supplier?.let { { it.get() } }
    }

    /**
     * Java-friendly setter for [preSelectedProvider].
     * Avoids requiring Java callers to implement a Kotlin
     * `Function0<Set<String>>`.
     */
    fun setPreSelectedProvider(supplier: JavaStringSetSupplier?) {
        preSelectedProvider = supplier?.let { { it.get() } }
    }
}