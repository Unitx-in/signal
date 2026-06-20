package com.unitx.signal_core.theme

import androidx.annotation.ColorInt

/**
 * Defines a custom color scheme for all signal types.
 * Any `null` property falls back to the default color for that element.
 *
 * Pass an instance via [SignalTheme] during initialization:
 * ```kotlin
 * Signal.createCore(this) {
 *     theme { colorScheme = ColorScheme(snackBackground = Color.BLACK) }
 * }
 * ```
 */
data class ColorScheme(

    // region Snack
    /** Background color of the snackbar container. */
    @ColorInt val snackBackground: Int? = null,
    /** Color of the snackbar message text. */
    @ColorInt val snackTextColor: Int? = null,
    /** Color of the custom action button text. */
    @ColorInt val snackActionTextColor: Int? = null,
    /** Tint applied to the cancel (✕) icon. */
    @ColorInt val snackCancelIconTint: Int? = null,
    /** Background tint of the snackbar icon. */
    @ColorInt val snackIconBackground: Int? = null,
    // endregion

    // region Toast
    /** Background color of the toast container. */
    @ColorInt val toastBackground: Int? = null,
    /** Stroke/border color of the toast container. */
    @ColorInt val toastStrokeColor: Int? = null,
    /** Color of the toast message text. */
    @ColorInt val toastTextColor: Int? = null,
    /** Tint applied to the toast icon. */
    @ColorInt val toastIconColor: Int? = null,
    // endregion

    // region Dialog
    /** Background color of the dialog card. */
    @ColorInt val dialogBackground: Int? = null,
    /** Primary accent color — used for the header label, icon, and buttons. */
    @ColorInt val dialogPrimaryColor: Int? = null,
    /** Secondary accent color — used for the header strip background. */
    @ColorInt val dialogSecondaryColor: Int? = null,
    /** Color of the title and message text. */
    @ColorInt val dialogTextColor: Int? = null,
    /** Text color of the primary (filled) button. */
    @ColorInt val dialogPrimaryButtonTextColor: Int? = null,
    // endregion

    // region Loading
    /** Three-stop gradient colors for the loading overlay background, as (start, center, end). */
    val loadingBackgroundGradient: Triple<Int, Int, Int>? = null,
    /** Color of the title and subtitle text on the loading overlay. */
    @ColorInt val loadingTextColor: Int? = null,
    /** Color of the active dot in the advanced loading animation. */
    @ColorInt val loadingAnimationActiveColor: Int? = null,
    /** Color of inactive dots in the advanced loading animation. */
    @ColorInt val loadingAnimationInactiveColor: Int? = null,
    /** Color of the active dot in the simple loading animation. */
    @ColorInt val loadingSimpleAnimationActiveColor: Int? = null,
    /** Color of inactive dots in the simple loading animation. */
    @ColorInt val loadingSimpleAnimationInactiveColor: Int? = null,
    /** Tint applied to the icon shown inside the loading animation ring. */
    @ColorInt val loadingIconColor: Int? = null,
    // endregion

)