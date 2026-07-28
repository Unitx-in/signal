package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unitx.signal_core.R
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.contract.config.dialog.DialogInputConfig
import com.unitx.signal_core.contract.model.FieldBinding

internal class DialogInputBinder(
    private val primaryColor: Int,
    private val dividerColor: Int
) {
    fun bindSingle(
        activity: Activity,
        config: DialogInputConfig,
        parent: ViewGroup,
        topMargin: Int,
        autoFocus: Boolean
    ): FieldBinding {
        val themedContext = ContextThemeWrapper(activity, com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar)

        val til = TextInputLayout(themedContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { this.topMargin = topMargin }
            boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            boxStrokeWidth = activity.dp(1)
            boxStrokeWidthFocused = activity.dp(2)
            setBoxCornerRadii(activity.dp(4).toFloat(), activity.dp(4).toFloat(), activity.dp(4).toFloat(), activity.dp(4).toFloat())
            setBoxStrokeColorStateList(ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
                intArrayOf(primaryColor, dividerColor)
            ))
            boxBackgroundColor = Color.TRANSPARENT
            hint = config.hint
            hintTextColor = ColorStateList.valueOf(primaryColor)
            defaultHintTextColor = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
                intArrayOf(primaryColor, dividerColor)
            )
            endIconMode = if (config.password) TextInputLayout.END_ICON_PASSWORD_TOGGLE else TextInputLayout.END_ICON_NONE
            setEndIconTintList(ColorStateList.valueOf(primaryColor))
            config.maxLength?.let { max ->
                if (config.showCounter) {
                    isCounterEnabled = true
                    counterMaxLength = max
                    counterTextColor = ColorStateList.valueOf(primaryColor)
                }
            }
        }

        val et = TextInputEditText(til.context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            inputType = when {
                config.password -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                config.multiLine -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                else -> config.inputType
            }
            typeface = ResourcesCompat.getFont(context, R.font.lora_reg)
            if (config.multiLine) { minLines = 3; maxLines = 6; gravity = Gravity.TOP or Gravity.START }
            if (config.maxLength != null) filters = arrayOf(InputFilter.LengthFilter(config.maxLength!!))
            if (config.prefill.isNotEmpty()) { setText(config.prefill); setSelection(config.prefill.length) }
        }

        til.addView(et)
        parent.addView(til)

        config.validator?.let { validate ->
            et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val value = s?.toString() ?: ""
                    til.error = if (!validate(value) && value.isNotEmpty()) config.validationError else null
                }
            })
        }


        if (autoFocus) {
            et.post {
                et.requestFocus()
                val imm = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        val commit: () -> Unit = { config.onInput?.invoke(et.text?.toString() ?: "") }
        val validate = {
            val value = et.text?.toString() ?: ""
            val valid = config.validator?.invoke(value) ?: true
            til.error = if (!valid) config.validationError else null
            valid
        }

        return FieldBinding(commit, validate)
    }
}