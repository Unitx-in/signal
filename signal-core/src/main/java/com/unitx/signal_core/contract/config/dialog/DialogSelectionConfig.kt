package com.unitx.signal_core.contract.config.dialog

import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.contract.type.DialogSelectionType

class DialogSelectionConfig {
    var mode: DialogSelectionType = DialogSelectionType.SINGLE
    var options: List<DialogSelectionOption> = emptyList()
    var preSelected: Set<String> = emptySet()
    var onSelected: ((Set<String>) -> Unit)? = null

    /** Convenience — plain string list */
    fun options(vararg labels: String) {
        options = labels.map { DialogSelectionOption(it) }
    }
}