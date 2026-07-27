package com.unitx.signal_core.contract.model

import com.unitx.signal_core.contract.config.dialog.DialogDropdownConfig
import com.unitx.signal_core.contract.config.dialog.DialogInputConfig
import com.unitx.signal_core.contract.config.dialog.DialogSelectionConfig

internal sealed interface DialogField {
    data class Input(val config: DialogInputConfig) : DialogField
    data class Selection(val config: DialogSelectionConfig) : DialogField
    data class Dropdown(val config: DialogDropdownConfig) : DialogField
}