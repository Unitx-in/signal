package com.unitx.signal_core.contract.config.dialog

import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.interop.JavaOptionsSupplier
import com.unitx.signal_core.interop.JavaUnitCallback

/**
 * Optional dropdown field configuration for a dialog — renders as a tappable
 * field that opens a popup list of single-select options.
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
 *     dropdown {
 *         placeholder = "Select sort order"
 *         optionsProvider = { fetchSortOptions() }
 *         preSelectedProvider = { currentSortOrder }
 *         autoDismissOnSelection = true
 *         onSelected = { selected -> }
 *     }
 *     positive("Apply") {}
 * }
 * ```
 */
class DialogDropdownConfig {

    /** Options to display, resolved from [optionsProvider] at show time. Empty if unset. */
    internal val options: List<DialogSelectionOption> get() = optionsProvider?.invoke() ?: emptyList()

    /** Selected value, resolved from [preSelectedProvider] at show time. Null if unset or none. */
    internal val preSelected: String? get() = preSelectedProvider?.invoke() ?: null

    /** If true (default), the popup auto-dismisses ~150ms after a selection is tapped.
     * If false, the popup stays open until the user taps outside
     * — useful if you want them to visually confirm the highlighted choice before it closes. */
    var autoDismissOnSelection: Boolean = true

    /** Text shown in the field before a selection is made. */
    var placeholder: String = "Select an option"

    /** Supplies the options list, evaluated lazily at show time. */
    var optionsProvider: (() -> List<DialogSelectionOption>)? = null
        @JvmName("setOptionsProviderKt") set

    /**
     * Supplies the default-selected value, evaluated lazily at show time.
     *
     * No Java-friendly wrapper needed — since this returns a nullable
     * `String`, not [Unit], a Java lambda already satisfies
     * `Function0<String>` directly, `null` included.
     */
    var preSelectedProvider: (() -> String?)? = null
        @JvmName("setPreSelectedProviderKt") set

    /** Called with the selected value (or null if none) when positive is tapped. */
    var onSelected: ((String?) -> Unit)? = null

    /** If provided, submit is blocked until this returns true for the current value. */
    var validator: ((String?) -> Boolean)? = null

    /** Error message shown below the field when [validator] returns false. */
    var validationError: String = ""

    /**
     * Java-friendly setter for [onSelected]. Avoids requiring `return null;`
     * from Java lambdas. Only invoked when a value has actually been selected.
     */
    fun onSelected(block: JavaUnitCallback<String>) {
        onSelected = { value -> value?.let { block.invoke(it) } }
    }

    /** Convenience — builds options from plain string labels. */
    fun options(vararg labels: String) {
        optionsProvider = { labels.map { DialogSelectionOption(it) } }
    }

    /**
     * Java-friendly setter for [optionsProvider].
     * Avoids requiring Java callers to implement a Kotlin
     * `Function0<List<DialogSelectionOption>>`.
     */
    fun setOptionsProvider(supplier: JavaOptionsSupplier?) {
        optionsProvider = supplier?.let { { it.get() } }
    }
}