package com.unitx.signal_core.contract.config.dialog

import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.contract.type.DialogSelectionType

/**
 * Optional selection list configuration for a dialog — renders as radio buttons,
 * checkboxes, or chips depending on [mode].
 *
 * Usage:
 * ```
 * Signal.dialog {
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
    var mode: DialogSelectionType = DialogSelectionType.SINGLE

    /** Options to display. */
    var options: List<DialogSelectionOption> = emptyList()

    /** Option values selected by default. */
    var preSelected: Set<String> = emptySet()

    /** Called with the final selected values when the positive button is tapped. */
    var onSelected: ((Set<String>) -> Unit)? = null

    /** Convenience — builds options from plain string labels. */
    fun options(vararg labels: String) {
        options = labels.map { DialogSelectionOption(it) }
    }
}