package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.R
import com.unitx.signal_core.contract.config.dialog.DialogDropdownConfig
import com.unitx.signal_core.contract.model.DialogSelectionOption
import com.unitx.signal_core.helper.dp
import androidx.core.graphics.toColorInt
import com.unitx.signal_core.contract.model.FieldBinding

internal class DialogDropdownBinder(
    private val primaryColor: Int,
    private val secondaryColor: Int,
    private val contentTextColor: Int,
    private val dividerColor: Int,
    private val autoDismissOnSelection: Boolean
) {

    fun bindSingle(
        activity: Activity,
        config: DialogDropdownConfig,
        parent: ViewGroup,
        topMargin: Int
    ): FieldBinding {
        val themedContext = ContextThemeWrapper(activity, R.style.Theme_MaterialComponents_Light_NoActionBar)
        var selectedValue: String? = config.preSelected
        var popupWindow: PopupWindow? = null

        val fieldBackground = GradientDrawable().apply {
            setStroke(activity.dp(1), dividerColor)
            cornerRadius = activity.dp(4).toFloat()
        }

        val fieldRow = buildFieldRow(themedContext, activity, fieldBackground).apply {
            (layoutParams as LinearLayout.LayoutParams).topMargin = topMargin
        }
        val fieldLabel = fieldRow.getChildAt(0) as TextView
        val fieldChevron = fieldRow.getChildAt(1) as TextView

        fun renderLabel() {
            val option = config.options.find { it.value == selectedValue }
            fieldLabel.text = option?.label ?: config.placeholder
            fieldLabel.setTextColor(if (option != null) contentTextColor else dividerColor)
        }
        renderLabel()

        fun setActiveState(active: Boolean) {
            fieldBackground.setStroke(activity.dp(if (active) 2 else 1), if (active) primaryColor else dividerColor)
            fieldChevron.setTextColor(if (active) primaryColor else dividerColor)
        }

        fieldRow.setOnClickListener {
            popupWindow?.dismiss()
            setActiveState(true)
            popupWindow = showOptionsPopup(
                context = themedContext, activity = activity, anchor = fieldRow,
                options = config.options, selectedValue = selectedValue,
                onOptionSelected = { value -> selectedValue = value; renderLabel() },
                onDismissed = { setActiveState(false) }
            )
        }

        val wrapper = LinearLayout(themedContext).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { this.topMargin = topMargin }
        }

        val errorText = TextView(themedContext).apply {
            setTextColor(ContextCompat.getColor(activity, android.R.color.holo_red_dark))
            textSize = 12f
            setPadding(0, activity.dp(4), 0, 0)
            visibility = View.GONE
        }
        wrapper.addView(fieldRow.apply { (layoutParams as LinearLayout.LayoutParams).topMargin = 0 })
        wrapper.addView(errorText)
        parent.addView(wrapper)

        val commit : () -> Unit = { config.onSelected?.invoke(selectedValue) }
        val validate = {
            val valid = config.validator?.invoke(selectedValue) ?: true
            errorText.visibility = if (!valid) View.VISIBLE else View.GONE
            errorText.text = config.validationError
            valid
        }

        return FieldBinding(commit, validate)
    }

    private fun buildFieldRow(themedContext: Context, activity: Activity, background: GradientDrawable): LinearLayout =
        LinearLayout(themedContext).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            this.background = background
            setPadding(activity.dp(12), activity.dp(10), activity.dp(12), activity.dp(10))
            isClickable = true
            isFocusable = true

            addView(TextView(themedContext).apply {
                typeface = ResourcesCompat.getFont(context, com.unitx.signal_core.R.font.lora_reg)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                textSize = 16f
            })
            addView(TextView(themedContext).apply {
                text = "▾"
                setTextColor(dividerColor)
                textSize = 16f
            })
        }

    private fun showOptionsPopup(
        context: Context,
        activity: Activity,
        anchor: View,
        options: List<DialogSelectionOption>,
        selectedValue: String?,
        onOptionSelected: (String) -> Unit,
        onDismissed: () -> Unit
    ): PopupWindow {
        var currentValue = selectedValue

        val listContainer = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable().apply {
                setColor(ContextCompat.getColor(activity, android.R.color.white))
                setStroke(activity.dp(1), dividerColor)
                cornerRadius = activity.dp(4).toFloat()
            }
        }

        lateinit var popupWindow: PopupWindow

        fun applyRowStyle(row: TextView, option: DialogSelectionOption) {
            val isSelected = option.value == currentValue
            row.setTextColor(if (isSelected) primaryColor else contentTextColor)
            row.setBackgroundColor(
                if (isSelected) "#1A000000".toColorInt()
                else ContextCompat.getColor(activity, android.R.color.white)
            )
        }

        options.forEach { option ->
            val row = TextView(context).apply {
                text = option.label
                textSize = 16f
                typeface = ResourcesCompat.getFont(context, com.unitx.signal_core.R.font.lora_reg)
                setPadding(activity.dp(16), activity.dp(12), activity.dp(16), activity.dp(12))
                isClickable = true
            }
            applyRowStyle(row, option)

            row.setOnClickListener {
                currentValue = option.value // update local state first
                for (i in 0 until listContainer.childCount) {
                    val child = listContainer.getChildAt(i) as TextView
                    applyRowStyle(child, options[i])
                }
                onOptionSelected(option.value)
                if (autoDismissOnSelection) {
                    listContainer.postDelayed({ popupWindow.dismiss() }, 150)
                }
            }

            listContainer.addView(row)
        }

        popupWindow = PopupWindow(listContainer, anchor.width, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            elevation = activity.dp(8).toFloat()
            isOutsideTouchable = true
            setOnDismissListener { onDismissed() }
            showAsDropDown(anchor, 0, activity.dp(4))
        }

        return popupWindow
    }
}