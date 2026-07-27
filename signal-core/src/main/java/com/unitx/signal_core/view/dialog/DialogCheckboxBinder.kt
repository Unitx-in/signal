package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.R
import com.unitx.signal_core.contract.config.dialog.DialogSelectionConfig
import com.unitx.signal_core.helper.dp

internal class DialogCheckboxBinder(
    private val primaryColor: Int,
    private val contentTextColor: Int
) {

    fun bindSingle(
        activity: Activity,
        selConfig: DialogSelectionConfig,
        parent: ViewGroup,
        topMargin: Int
    ): () -> Unit {
        val themedContext = ContextThemeWrapper(activity, R.style.Theme_MaterialComponents_Light_NoActionBar)
        val selected = selConfig.preSelected.toMutableSet()

        val wrapper = LinearLayout(themedContext).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { this.topMargin = topMargin }
        }

        if (selConfig.label.isNotBlank()) {
            wrapper.addView(buildLabel(themedContext, activity, selConfig.label))
        }

        val checkboxGroup = LinearLayout(themedContext).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        selConfig.options.forEach { option ->
            val cb = CheckBox(themedContext).apply {
                text = option.label
                isChecked = option.value in selected
                buttonTintList = ColorStateList.valueOf(primaryColor)
                typeface = ResourcesCompat.getFont(context, com.unitx.signal_core.R.font.lora_reg)
                setTextColor(contentTextColor)
                setPadding(activity.dp(8), activity.dp(4), 0, activity.dp(4))
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selected.add(option.value) else selected.remove(option.value)
                }
            }
            checkboxGroup.addView(cb)
        }

        wrapper.addView(checkboxGroup)
        parent.addView(wrapper)

        return {
            selConfig.onSelected?.invoke(selected)
        }
    }

    private fun buildLabel(context: Context, activity: Activity, text: String): TextView =
        TextView(context).apply {
            this.text = text
            textSize = 13f
            setTextColor(contentTextColor)
            setPadding(0, 0, 0, activity.dp(6))
            typeface = ResourcesCompat.getFont(context, com.unitx.signal_core.R.font.poppins_med)
        }
}