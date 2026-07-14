package com.unitx.signal_core.theme

import androidx.annotation.ColorInt

/**
 * DSL builder for constructing a [ColorScheme]. Use this when you prefer setting
 * colors imperatively rather than via the [ColorScheme] constructor.
 *
 * ```kotlin
 * val scheme = ColorSchemeBuilder().apply {
 *     snackBackground = Color.BLACK
 *     dialogPrimaryColor = Color.BLUE
 * }.build()
 * ```
 *
 * See [ColorScheme] for a description of each property.
 */
class ColorSchemeBuilder {

    // region Snack
    @ColorInt var snackBackground: Int? = null
    @ColorInt var snackTextColor: Int? = null
    @ColorInt var snackActionTextColor: Int? = null
    @ColorInt var snackCancelIconTint: Int? = null
    // endregion

    // region Toast
    @ColorInt var toastBackground: Int? = null
    @ColorInt var toastTextColor: Int? = null
    @ColorInt var toastStrokeColor: Int? = null
    @ColorInt var toastIconColor: Int? = null
    // endregion

    // region Dialog
    @ColorInt var dialogBackground: Int? = null
    @ColorInt var dialogPrimaryColor: Int? = null
    @ColorInt var dialogSecondaryColor: Int? = null
    @ColorInt var dialogTextColor: Int? = null
    @ColorInt var dialogPrimaryButtonTextColor: Int? = null
    // endregion

    // region Loading
    var loadingBackground: Triple<Int, Int, Int>? = null
    @ColorInt var loadingTextColor: Int? = null
    @ColorInt var loadingAnimationActiveColor: Int? = null
    @ColorInt var loadingAnimationInactiveColor: Int? = null
    @ColorInt var loadingSimpleAnimationActiveColor: Int? = null
    @ColorInt var loadingSimpleAnimationInactiveColor: Int? = null
    @ColorInt var loadingIconColor: Int? = null
    // endregion

    // region Notification
    @ColorInt var notifBackground: Int? = null
    @ColorInt var notifTextColor: Int? = null
    @ColorInt var notifHighlightTextColor: Int? = null
    // endregion

    /** Builds and returns a [ColorScheme] from the current property values. */
    fun build() = ColorScheme(
        snackBackground = snackBackground,
        snackTextColor = snackTextColor,
        snackActionTextColor = snackActionTextColor,
        snackCancelIconTint = snackCancelIconTint,
        toastBackground = toastBackground,
        toastTextColor = toastTextColor,
        dialogBackground = dialogBackground,
        dialogPrimaryColor = dialogPrimaryColor,
        dialogSecondaryColor = dialogSecondaryColor,
        dialogTextColor = dialogTextColor,
        dialogPrimaryButtonTextColor = dialogPrimaryButtonTextColor,
        toastStrokeColor = toastStrokeColor,
        toastIconColor = toastIconColor,
        loadingBackgroundGradient = loadingBackground,
        loadingTextColor = loadingTextColor,
        loadingAnimationActiveColor = loadingAnimationActiveColor,
        loadingAnimationInactiveColor = loadingAnimationInactiveColor,
        loadingIconColor = loadingIconColor,
        loadingSimpleAnimationActiveColor = loadingSimpleAnimationActiveColor,
        loadingSimpleAnimationInactiveColor = loadingSimpleAnimationInactiveColor,
        notifBackground = notifBackground,
        notifTextColor = notifTextColor,
        notifHighlightTextColor = notifHighlightTextColor,
    )
}