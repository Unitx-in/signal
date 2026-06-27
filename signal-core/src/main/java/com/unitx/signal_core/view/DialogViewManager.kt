package com.unitx.signal_core.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.unitx.signal_core.R
import com.unitx.signal_core.contract.config.DialogConfig
import com.unitx.signal_core.theme.ThemeResolver
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.helper.DimOverlay
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.helper.rootViewGroup
import com.unitx.signal_core.provider.ActivityProvider

internal class DialogViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: ThemeResolver
) {

    private var binding: SignalDialogBinding? = null
    private val dim = DimOverlay()

    val container: View?
        get() = binding?.dialogCard

    val isShowing: Boolean
        get() = binding?.dialogCard?.visibility == View.VISIBLE

    fun attach(config: DialogConfig, onDismiss: () -> Unit): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.rootViewGroup() ?: return false

        dim.attach(activity, rootView, config.cancelable, onDismiss)
        inflateDialog(activity, rootView, config.horizontalMargin)
        applyTheme(activity, config)
        bind(config, onDismiss)
        return true
    }

    private fun inflateDialog(context: Context, rootView: ViewGroup, horizontalMargin: Int) {
        if (binding != null) return
        val themedContext = ContextThemeWrapper(
            context,
            com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar
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
        val textColor = scheme.dialogTextColor ?: color(R.color.signalBlack)
        val primaryBtnTextColor = scheme.dialogPrimaryButtonTextColor ?: color(android.R.color.white)

        b.dialogCard.setCardBackgroundColor(scheme.dialogBackground ?: color(android.R.color.white))
        b.dialogHeaderBg.setBackgroundColor(secondary)
        b.dialogHeaderLabel.setTextColor(primary)
        b.dialogClose.imageTintList = ColorStateList.valueOf(primary)
        b.dialogIcon.imageTintList = ColorStateList.valueOf(primary)
        b.dialogTitle.setTextColor(textColor)
        b.dialogMessage.setTextColor(textColor)
        (b.dialogPrimaryBtn.background as? GradientDrawable)?.setColor(primary)
        b.dialogPrimaryBtn.setTextColor(primaryBtnTextColor)
        (b.dialogSecondaryBtn.background as? GradientDrawable)?.setStroke(1, primary)
        b.dialogSecondaryBtn.setTextColor(primary)
        b.dialogNeutralText.setTextColor(primary)
        b.dialogInputLayout.boxStrokeColor = primary
        b.dialogInputLayout.hintTextColor = ColorStateList.valueOf(primary)
        b.dialogInputLayout.setEndIconTintList(ColorStateList.valueOf(primary))
        b.dialogInputLayout.counterTextColor = ColorStateList.valueOf(textColor)
    }

    private fun bind(config: DialogConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.dialogTitle.text = config.title
        b.dialogCard.contentDescription = config.accessibilityText ?: "${config.title}. ${config.message}"
        b.dialogMessage.text = config.message
        b.dialogClose.setOnClickListener { onDismiss() }
        b.dialogIcon.setImageResource(config.icon ?: config.type.icon)

        config.header.takeIf { it.isNotBlank() }?.let { h ->
            b.dialogHeaderLabel.text = h
        } ?: run {
            b.dialogHeaderLabel.text = ContextCompat.getString(b.root.context, config.type.header)
        }

        config.positive?.let { (label, onClick) ->
            b.dialogPrimaryBtn.visibility = View.VISIBLE
            b.dialogPrimaryBtn.text = label
            b.dialogPrimaryBtn.setOnClickListener {
                onClick()
                if (config.dismissOnPositive) onDismiss()
            }
        } ?: run { b.dialogPrimaryBtn.visibility = View.GONE }

        config.negative?.let { (label, onClick) ->
            b.dialogSecondaryBtn.visibility = View.VISIBLE
            b.dialogSecondaryBtn.text = label
            b.dialogSecondaryBtn.setOnClickListener {
                onClick()
                if (config.dismissOnNegative) onDismiss()
            }
        } ?: run { b.dialogSecondaryBtn.visibility = View.GONE }

        config.neutral?.let { (label, onClick) ->
            b.dialogNeutralText.visibility = View.VISIBLE
            b.dialogNeutralText.text = label
            b.dialogNeutralText.setOnClickListener {
                onClick()
                if (config.dismissOnNeutral) onDismiss()
            }
        } ?: run { b.dialogNeutralText.visibility = View.GONE }

        config.input?.let { inputConfig ->
            val til = b.dialogInputLayout
            val et = b.dialogInput

            til.visibility = View.VISIBLE
            til.hint = inputConfig.hint

            // input type
            et.inputType = when {
                inputConfig.password -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                inputConfig.multiLine -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                else -> inputConfig.inputType
            }

            if (inputConfig.multiLine) {
                et.minLines = 3
                et.maxLines = 6
                et.gravity = Gravity.TOP or Gravity.START
            }

            til.endIconMode = if (inputConfig.password)
                TextInputLayout.END_ICON_PASSWORD_TOGGLE
            else
                TextInputLayout.END_ICON_NONE

            inputConfig.maxLength?.let { max ->
                et.filters = arrayOf(InputFilter.LengthFilter(max))
                if (inputConfig.showCounter) {
                    til.isCounterEnabled = true
                    til.counterMaxLength = max
                }
            }

            if (inputConfig.prefill.isNotEmpty()) {
                et.setText(inputConfig.prefill)
                et.setSelection(inputConfig.prefill.length)
            }

            inputConfig.validator?.let { validate ->
                val primaryBtn = b.dialogPrimaryBtn
                primaryBtn.isEnabled = validate(inputConfig.prefill)
                et.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        val value = s?.toString() ?: ""
                        val valid = validate(value)
                        primaryBtn.isEnabled = valid
                        til.error = if (!valid && value.isNotEmpty()) inputConfig.validationError else null
                    }
                })
            }

            et.post {
                et.requestFocus()
                val imm = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
            }

            inputConfig.onInput?.let { callback ->
                b.dialogPrimaryBtn.setOnClickListener {
                    til.error = null
                    callback(et.text?.toString() ?: "")
                    if (config.dismissOnPositive) onDismiss()
                }
            }
        } ?: run {
            b.dialogInputLayout.visibility = View.GONE
        }
    }

    fun release(onReleased: () -> Unit = {}) {
        binding?.dialogInput?.let { et ->
            val imm = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et.windowToken, 0)
        }
        val rootView = activityProvider.current()?.rootViewGroup()
        dim.release(rootView) {
            binding?.root?.let { rootView?.removeView(it) }
            binding = null
            onReleased()
        }
    }
}