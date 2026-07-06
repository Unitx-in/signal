package com.unitx.signal_core.view.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.contract.config.dialog.DialogScope
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.activity.ActivityProvider

internal class DialogInputBinder(
    private val activityProvider: ActivityProvider,
    private val primaryColor: Int,
    private val dividerColor: Int
) {

    fun bind(config: DialogConfig, b: SignalDialogBinding, onDismiss: () -> Unit) {
        val inputContainer = b.dialogInputContainer
        inputContainer.removeAllViews()

        if (config.inputs.isEmpty()) {
            inputContainer.visibility = View.GONE
            return
        }

        inputContainer.visibility = View.VISIBLE
        val activity = activityProvider.current() ?: return
        val themedContext = ContextThemeWrapper(
            activity,
            com.google.android.material.R.style.Theme_MaterialComponents_Light_NoActionBar
        )
        val fields = mutableListOf<Pair<TextInputLayout, TextInputEditText>>()

        config.inputs.forEachIndexed { index, inputConfig ->
            val til = TextInputLayout(themedContext).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { if (index > 0) topMargin = activity.dp(12) }
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
                boxStrokeWidth = activity.dp(1)
                boxStrokeWidthFocused = activity.dp(2)
                setBoxCornerRadii(
                    activity.dp(4).toFloat(), activity.dp(4).toFloat(),
                    activity.dp(4).toFloat(), activity.dp(4).toFloat()
                )
                setBoxStrokeColorStateList(ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
                    intArrayOf(primaryColor, dividerColor)
                ))
                boxBackgroundColor = Color.TRANSPARENT
                hint = inputConfig.hint
                hintTextColor = ColorStateList.valueOf(primaryColor)
                defaultHintTextColor = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
                    intArrayOf(primaryColor, dividerColor)
                )
                endIconMode = if (inputConfig.password)
                    TextInputLayout.END_ICON_PASSWORD_TOGGLE
                else
                    TextInputLayout.END_ICON_NONE
                setEndIconTintList(ColorStateList.valueOf(primaryColor))
                inputConfig.maxLength?.let { max ->
                    if (inputConfig.showCounter) {
                        isCounterEnabled = true
                        counterMaxLength = max
                        counterTextColor = ColorStateList.valueOf(primaryColor)
                    }
                }
            }

            val et = TextInputEditText(til.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                inputType = when {
                    inputConfig.password -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    inputConfig.multiLine -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    else -> inputConfig.inputType
                }
                if (inputConfig.multiLine) {
                    minLines = 3; maxLines = 6
                    gravity = Gravity.TOP or Gravity.START
                }
                if (inputConfig.maxLength != null) filters = arrayOf(InputFilter.LengthFilter(inputConfig.maxLength!!))
                if (inputConfig.prefill.isNotEmpty()) {
                    setText(inputConfig.prefill)
                    setSelection(inputConfig.prefill.length)
                }
            }

            til.addView(et)
            inputContainer.addView(til)
            fields.add(til to et)

            inputConfig.validator?.let { validate ->
                b.dialogPrimaryBtn.isEnabled = validate(inputConfig.prefill)
                et.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        val value = s?.toString() ?: ""
                        val valid = validate(value)
                        b.dialogPrimaryBtn.isEnabled = valid
                        til.error = if (!valid && value.isNotEmpty()) inputConfig.validationError else null
                    }
                })
            }
        }

        fields.firstOrNull()?.second?.let { firstEt ->
            firstEt.post {
                firstEt.requestFocus()
                val imm = firstEt.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(firstEt, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        b.dialogPrimaryBtn.setOnClickListener {
            val scope = DialogScope()
            config.positive?.second?.let { scope.it() }
            val values = fields.map { (_, et) -> et.text?.toString() ?: "" }
            config.inputs.forEachIndexed { i, inputConfig ->
                inputConfig.onInput?.invoke(values.getOrElse(i) { "" })
            }
            if (config.dismissOnPositive && scope.shouldDismiss) onDismiss()
        }
    }
}