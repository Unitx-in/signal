package com.unitx.signal_core.contract.config.dialog

import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.contract.type.DialogSelectionMode
import com.unitx.signal_core.interop.JavaUnitCallback

/**
 * Optional selection list configuration for a dialog — renders as radio buttons,
 * checkboxes, or chips depending on [mode].
 *
 * Usage:
 * ```
 * Signal.dialog(this) {
 *     title = "Sort by"
 *     selection {
 *         mode = DialogSelectionType.SINGLE
 *         options("Name", "Date", "Size")
 *         preSelected = setOf("Name")
 *         onSelected = { selected -> }
 *     }
 *     positive("Apply") {}
 * }
 * ```
 */
class DialogSelectionConfig {

    /** Selection UI style: SINGLE (radio), MULTI (checkbox), or CHIP. */
    var mode: DialogSelectionMode = DialogSelectionMode.SINGLE

    /** Options to display. */
    var options: List<DialogSelectionOption> = emptyList()

    /** Option values selected by default. */
    var preSelected: Set<String> = emptySet()

    /** Called with the final selected values when the positive button is tapped. */
    var onSelected: ((Set<String>) -> Unit)? = null

    /**
     * Java-friendly setter for [onSelected]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called with the final selected values when the positive button is tapped.
     */
    fun onSelected(block: JavaUnitCallback<Set<String>>) {
        onSelected = { block.invoke(it) }
    }

    /** Convenience — builds options from plain string labels. */
    fun options(vararg labels: String) {
        options = labels.map { DialogSelectionOption(it) }
    }
}