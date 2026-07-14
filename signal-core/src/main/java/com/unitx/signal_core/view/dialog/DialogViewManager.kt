package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.method.ScrollingMovementMethod
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.R
import com.google.android.material.textfield.TextInputLayout
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.contract.config.dialog.DialogScope
import com.unitx.signal_core.contract.type.DialogSelectionMode
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.helper.DimOverlay
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.helper.rootViewGroup
import com.unitx.signal_core.activity.ActivityProvider
import com.unitx.signal_core.theme.ThemeResolver

internal class DialogViewManager(
    private val themeResolver: ThemeResolver
) {

    private var binding: SignalDialogBinding? = null
    private var attachedActivity: Activity? = null
    private val dim = DimOverlay()

    private var primaryColor: Int = 0
    private var contentTextColor: Int = 0
    private var dividerColor: Int = 0
    private var secondaryColor: Int = 0

    val container: View?
        get() = binding?.dialogCard

    val isShowing: Boolean
        get() = binding?.dialogCard?.visibility == View.VISIBLE

    fun attach(activity: Activity, config: DialogConfig, onDismiss: () -> Unit): Boolean {
        val rootView = activity.rootViewGroup() ?: return false
        attachedActivity = activity

        dim.attach(activity, rootView, config.cancelable, onDismiss)
        inflateDialog(activity, rootView, config.horizontalMargin)
        applyTheme(activity, config)
        bind(activity, config, onDismiss)
        return true
    }

    private fun inflateDialog(context: Context, rootView: ViewGroup, horizontalMargin: Int) {
        if (binding != null) return
        val themedContext = ContextThemeWrapper(
            context,
            R.style.Theme_MaterialComponents_Light_NoActionBar
        )
        binding = SignalDialogBinding.inflate(LayoutInflater.from(themedContext), rootView, false)
        rootView.addView(
            binding!!.root,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                val margin = context.dp(horizontalMargin)
                leftMargin = margin
                rightMargin = margin
            }
        )
    }

    private fun applyTheme(context: Context, config: DialogConfig) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)
        val type = config.type

        fun color(@ColorRes res: Int) = ContextCompat.getColor(context, res)
        val primary = scheme.dialogPrimaryColor ?: color(type.primaryColor)
        val secondary = scheme.dialogSecondaryColor ?: color(type.secondaryColor)
        val textColor = scheme.dialogTextColor ?: color(com.unitx.signal_core.R.color.signalBlack)
        val primaryBtnTextColor =
            scheme.dialogPrimaryButtonTextColor ?: color(android.R.color.white)

        b.dialogCard.setCardBackgroundColor(scheme.dialogBackground ?: color(android.R.color.white))
        b.dialogHeaderBg.setBackgroundColor(secondary)
        b.dialogHeaderLabel.setTextColor(primary)
        b.dialogClose.imageTintList = ColorStateList.valueOf(primary)
        b.dialogIcon.imageTintList = ColorStateList.valueOf(primary)
        b.dialogTitle.setTextColor(textColor)
        b.dialogMessage.setTextColor(textColor)
        b.dialogPrimaryBtn.backgroundTintList = ColorStateList.valueOf(primary)
        b.dialogPrimaryBtn.setTextColor(primaryBtnTextColor)
        b.dialogSecondaryBtn.backgroundTintList = null
        (b.dialogSecondaryBtn.background.mutate() as GradientDrawable)
            .setStroke(context.dp(config.secondaryButtonStrokeWidth), primary)
        b.dialogSecondaryBtn.setTextColor(primary)
        b.dialogNeutralText.setTextColor(primary)
        primaryColor = primary
        contentTextColor = textColor
        dividerColor = color(com.unitx.signal_core.R.color.signalDivider)
        secondaryColor = secondary
    }

    private fun bind(activity: Activity, config: DialogConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.dialogTitle.text = config.title
        b.dialogCard.contentDescription =
            config.accessibilityText ?: "${config.title}. ${config.message}"
        b.dialogMessage.text = config.message
        b.dialogMessage.movementMethod = ScrollingMovementMethod.getInstance()
        b.dialogClose.setOnClickListener { onDismiss() }
        b.dialogIcon.setImageResource(config.icon ?: config.type.icon)
        b.dialogClose.visibility = if (config.showCloseButton) View.VISIBLE else View.GONE

        config.header.takeIf { it.isNotBlank() }?.let { h ->
            b.dialogHeaderLabel.text = h
        } ?: run {
            b.dialogHeaderLabel.text = ContextCompat.getString(b.root.context, config.type.header)
        }

        config.positive?.let { (label, onClick) ->
            b.dialogPrimaryBtn.visibility = View.VISIBLE
            b.dialogPrimaryBtn.text = label
            b.dialogPrimaryBtn.setOnClickListener {
                val scope = DialogScope()
                scope.onClick()
                if (config.dismissOnPositive && scope.shouldDismiss) onDismiss()
            }
        } ?: run { b.dialogPrimaryBtn.visibility = View.GONE }

        config.negative?.let { (label, onClick) ->
            b.dialogSecondaryBtn.visibility = View.VISIBLE
            b.dialogSecondaryBtn.text = label
            b.dialogSecondaryBtn.setOnClickListener {
                val scope = DialogScope()
                scope.onClick()
                if (config.dismissOnNegative && scope.shouldDismiss) onDismiss()
            }
        } ?: run { b.dialogSecondaryBtn.visibility = View.GONE }

        config.neutral?.let { (label, onClick) ->
            b.dialogNeutralText.visibility = View.VISIBLE
            b.dialogNeutralText.text = label
            b.dialogNeutralText.setOnClickListener {
                val scope = DialogScope()
                scope.onClick()
                if (config.dismissOnNeutral && scope.shouldDismiss) onDismiss()
            }
        } ?: run { b.dialogNeutralText.visibility = View.GONE }

        bindInputs(activity, config, b, onDismiss)
        bindSelection(activity, config, b, onDismiss)
    }

    private fun bindInputs(activity: Activity, config: DialogConfig, b: SignalDialogBinding, onDismiss: () -> Unit) {
        DialogInputBinder(primaryColor, dividerColor)
            .bind(activity, config, b, onDismiss)
    }

    private fun bindSelection(activity: Activity, config: DialogConfig, b: SignalDialogBinding, onDismiss: () -> Unit) {
        val selConfig = config.selection ?: run {
            b.dialogSelectionContainer.visibility = View.GONE
            return
        }
        when (selConfig.mode) {
            DialogSelectionMode.CHIP ->
                DialogChipBinder(primaryColor, secondaryColor)
                    .bind(activity, config, b, onDismiss)
            DialogSelectionMode.SINGLE ->
                DialogRadioBinder(primaryColor, contentTextColor)
                    .bind(activity, config, b, onDismiss)
            DialogSelectionMode.MULTI ->
                DialogCheckboxBinder(primaryColor, contentTextColor)
                    .bind(activity, config, b, onDismiss)
        }
    }

    fun release(onReleased: () -> Unit = {}) {

        val inputContainer = binding?.dialogInputContainer
        val firstEt = (0 until (inputContainer?.childCount ?: 0))
            .firstNotNullOfOrNull { inputContainer?.getChildAt(it) as? TextInputLayout }
            ?.editText
        firstEt?.let {
            val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding?.dialogInputContainer?.removeAllViews()
        binding?.dialogSelectionContainer?.removeAllViews()

        val rootView = attachedActivity?.rootViewGroup()
        dim.release(rootView) {
            binding?.root?.let { rootView?.removeView(it) }
            binding = null
            attachedActivity = null
            onReleased()
        }
    }
}