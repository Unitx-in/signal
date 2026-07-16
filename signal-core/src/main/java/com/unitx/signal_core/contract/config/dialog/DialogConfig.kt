package com.unitx.signal_core.contract.config.dialog

import androidx.annotation.DrawableRes
import com.unitx.signal_core.contract.type.DialogType

/**
 * Configuration for a dialog signal.
 *
 * Usage:
 * ```
 * Signal.dialog(this) {
 *     title = "Delete file?"
 *     message = "This action cannot be undone."
 *     type = DialogType.Warning
 *     positive("Delete") { deleteFile() }
 *     negative("Cancel")
 * }
 * ```
 */
class DialogConfig {

    internal var positive: Pair<String, DialogScope.() -> Unit>? = null
    internal var negative: Pair<String, DialogScope.() -> Unit>? = null
    internal var neutral: Pair<String, DialogScope.() -> Unit>? = null

    internal var inputs: List<DialogInputConfig> = emptyList()
    internal var selection: DialogSelectionConfig? = null
    internal var secondaryButtonStrokeWidth: Int = 2

    /** Main heading of the dialog. */
    var title: String = ""

    /** Body text shown below the title. */
    var message: String = ""

    /** Optional label shown in the colored header strip. Defaults to the [type] label if blank. */
    var header: String = ""

    /** Optional icon. Defaults to the [type] icon if null. */
    @DrawableRes
    var icon: Int? = null

    /** Optional remote icon, loaded async. Takes precedence over [icon] if both are set. */
    var iconUrl: String? = null

    /** Visual style of the dialog — affects colors and default icon/header. Default: [DialogType.Default]. */
    var type: DialogType = DialogType.Default

    /** If true, tapping outside the dialog dismisses it. Default: false. */
    var cancelable: Boolean = false

    /** Horizontal margin from screen edges in dp. Default: 24. */
    var horizontalMargin: Int = 24

    /** If true, dialog auto-dismisses after [autoDismissDuration] ms. Default: false. */
    var autoDismiss: Boolean = false

    /** Duration in ms before auto-dismiss. Default: 4000. */
    var autoDismissDuration: Long = 4000L

    /** If true, dialog dismisses when the positive button is tapped. Default: true. */
    var dismissOnPositive: Boolean = true

    /** If true, dialog dismisses when the negative button is tapped. Default: true. */
    var dismissOnNegative: Boolean = true

    /** If true, dialog dismisses when the neutral text is tapped. Default: true. */
    var dismissOnNeutral: Boolean = true

    /** If true, it shows the dialog close button at the top right. Default: true. */
    var showCloseButton : Boolean = true

    /** Called when the dialog becomes visible. */
    var onShown: (() -> Unit)? = null

    /** Called when the dialog is dismissed for any reason. */
    var onDismissed: (() -> Unit)? = null

    /** Overrides the default accessibility description. */
    var accessibilityText: String? = null

    /* Disables loading icon color as icon url can fetch images. */
    var disableIconColor: Boolean = false

    /** Adds a primary (filled) button. */
    fun positive(label: String, onClick: DialogScope.() -> Unit = {}) {
        positive = label to onClick
    }

    /** Adds a secondary (outlined) button. */
    fun negative(label: String, strokeWidth: Int = 2, onClick: DialogScope.() -> Unit = {}) {
        secondaryButtonStrokeWidth = strokeWidth
        negative = label to onClick
    }

    /** Adds a text-only neutral action below the buttons. */
    fun neutral(label: String, onClick: DialogScope.() -> Unit = {}) {
        neutral = label to onClick
    }

    /** Adds a text input field to the dialog. Call multiple times to stack fields. */
    fun input(block: DialogInputConfig.() -> Unit) {
        inputs = inputs + DialogInputConfig().apply(block)
    }

    /** Adds a selection list (radio/checkbox/chip) to the dialog. */
    fun selection(block: DialogSelectionConfig.() -> Unit) {
        selection = DialogSelectionConfig().apply(block)
    }

    internal fun copy(): DialogConfig = DialogConfig().also {
        it.title = title
        it.message = message
        it.type = type
        it.cancelable = cancelable
        it.autoDismiss = autoDismiss
        it.autoDismissDuration = autoDismissDuration
        it.icon = icon
        it.iconUrl = iconUrl
        it.header = header
        it.horizontalMargin = horizontalMargin
        it.positive = positive
        it.negative = negative
        it.neutral = neutral
        it.dismissOnPositive = dismissOnPositive
        it.dismissOnNegative = dismissOnNegative
        it.onShown = onShown
        it.onDismissed = onDismissed
        it.accessibilityText = accessibilityText
        it.inputs = inputs
        it.selection = selection
        it.showCloseButton = showCloseButton
        it.secondaryButtonStrokeWidth = secondaryButtonStrokeWidth
        it.disableIconColor = disableIconColor
    }
}