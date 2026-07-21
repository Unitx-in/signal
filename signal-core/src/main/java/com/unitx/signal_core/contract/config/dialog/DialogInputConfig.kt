package com.unitx.signal_core.contract.config.dialog

import android.text.InputType
import com.unitx.signal_core.interop.JavaUnitCallback

/**
 * Optional text input configuration for a dialog.
 *
 * A dialog can have multiple inputs — call [DialogConfig.input] more than once
 * to stack fields (e.g. username + password).
 *
 * Usage:
 * Signal.dialog(this) {
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

    /**
     * If provided, positive button is disabled until this returns true.
     *
     * Note: no Java-friendly overload is needed here — since this returns
     * [Boolean] rather than [Unit], Java lambdas already satisfy
     * `Function1<String, Boolean>` directly (e.g. `input -> input.isNotBlank()`)
     * without requiring `return null;`.
     */
    var validator: ((String) -> Boolean)? = null

    /** Error message shown below the field when [validator] returns false. */
    var validationError: String = ""

    /** Called with the current input value when positive is tapped. */
    var onInput: ((String) -> Unit)? = null

    /**
     * Java-friendly setter for [onInput]. Avoids requiring `return null;`
     * from Java lambdas.
     *
     * Called with the current input value when positive is tapped.
     */
    fun onInput(block: JavaUnitCallback<String>) {
        onInput = { block.invoke(it) }
    }
}