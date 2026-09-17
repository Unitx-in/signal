package com.unitx.signal_core.contract.config.dialog

import android.text.InputType
import com.unitx.signal_core.interop.JavaStringSupplier
import com.unitx.signal_core.interop.JavaUnitCallback

/**
 * Optional text input configuration for a dialog.
 *
 * A dialog can have multiple inputs — call [DialogConfig.input] more than once
 * to stack fields (e.g. username + password).
 *
 * [prefillProvider] is evaluated lazily at show time rather than when this
 * config block runs, so it reflects the current value even if the dialog
 * sits in a queue behind other signals before it's actually shown.
 *
 * Usage:
 * ```kotlin
 * Signal.dialog(this) {
 *     title = "Rename file"
 *     input {
 *         hint = "File name"
 *         prefillProvider = { currentName }
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

    /** Pre-filled value, resolved from [prefillProvider] at show time. Empty if unset. */
    internal val prefill: String get() = prefillProvider?.invoke() ?: ""

    /** Hint text shown inside the input field. */
    var hint: String = ""

    /** Supplies the pre-filled value, evaluated lazily at show time. */
    var prefillProvider: (() -> String)? = null
        @JvmName("setPrefillProviderKt") set

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
     */
    fun onInput(block: JavaUnitCallback<String>) {
        onInput = { block.invoke(it) }
    }

    /**
     * Java-friendly setter for [prefillProvider].
     * Avoids requiring Java callers to implement a Kotlin `Function0<String>`.
     */
    fun setPrefillProvider(supplier: JavaStringSupplier?) {
        prefillProvider = supplier?.let { { it.get() } }
    }
}