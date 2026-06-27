package com.unitx.signal_core.contract.config.dialog

import android.text.InputType

/**
 * Optional text input configuration for a dialog.
 *
 * Usage:
 * ```
 * Signal.dialog {
 *     title = "Rename file"
 *     input {
 *         hint = "File name"
 *         prefill = currentName
 *         maxLength = 50
 *         showCounter = true
 *         validator = { it.isNotBlank() }
 *         validationError = "Name cannot be empty"
 *     }
 *     positive("Rename") { /* value via onInput */ }
 * }
 * ```
 */
class DialogInputConfig {

    /** Hint text shown inside the input field. */
    var hint: String = ""

    /** Pre-filled value. */
    var prefill: String = ""

    /** Android [InputType] flags. Default: plain text. */
    var inputType: Int = InputType.TYPE_CLASS_TEXT

    /** Max character length. Null = no limit. */
    var maxLength: Int? = null

    /** Show character counter below the field. Requires [maxLength]. */
    var showCounter: Boolean = false

    /** If true, input is masked (password mode) with a visibility toggle. */
    var password: Boolean = false

    /** If true, input expands to multi-line. */
    var multiLine: Boolean = false

    /** If provided, positive button is disabled until this returns true. */
    var validator: ((String) -> Boolean)? = null

    /** Error message shown below the field when [validator] returns false. */
    var validationError: String = ""

    /** Called with the current input value when positive is tapped. */
    var onInput: ((String) -> Unit)? = null
}