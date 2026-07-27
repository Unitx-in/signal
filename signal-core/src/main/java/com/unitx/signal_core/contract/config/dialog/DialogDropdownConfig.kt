package com.unitx.signal_core.contract.config.dialog

import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.interop.JavaUnitCallback

/**
 * Optional dropdown field configuration for a dialog — renders as a tappable
 * field that opens a popup list of single-select options.
 *
 * Usage:
 * ```
 * Signal.dialog(this) {
 *     title = "Sort by"
 *     dropdown {
 *         placeholder = "Select sort order"
 *         options("Name", "Date", "Size")
 *         preSelected = "Name"
 *         onSelected = { selected -> }
 *     }
 *     positive("Apply") {}
 * }
 * ```
 */
class DialogDropdownConfig {

    /** Text shown in the field before a selection is made. */
    var placeholder: String = "Select an option"

    /** Options to display in the popup list. */
    var options: List<DialogSelectionOption> = emptyList()

    /** Option value selected by default. Null = none. */
    var preSelected: String? = null

    /** Called with the selected value (or null if none) when positive is tapped. */
    var onSelected: ((String?) -> Unit)? = null

    /**
     * Java-friendly setter for [onSelected]. Avoids requiring `return null;`
     * from Java lambdas. Only invoked when a value has actually been selected.
     *
     * Called with the selected value when positive is tapped.
     */
    fun onSelected(block: JavaUnitCallback<String>) {
        onSelected = { value -> value?.let { block.invoke(it) } }
    }

    /** Convenience — builds options from plain string labels. */
    fun options(vararg labels: String) {
        options = labels.map { DialogSelectionOption(it) }
    }
}