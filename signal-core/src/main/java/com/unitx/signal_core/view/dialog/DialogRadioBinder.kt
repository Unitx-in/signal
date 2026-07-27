package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.R
import com.unitx.signal_core.contract.config.dialog.DialogSelectionConfig
import com.unitx.signal_core.helper.dp

internal class DialogRadioBinder(
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

        val radioGroup = RadioGroup(themedContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = RadioGroup.VERTICAL
        }

        selConfig.options.forEach { option ->
            val rb = RadioButton(themedContext).apply {
                text = option.label
                id = View.generateViewId()
                isChecked = option.value in selected
                buttonTintList = ColorStateList.valueOf(primaryColor)
                setTextColor(contentTextColor)
                setPadding(activity.dp(8), activity.dp(4), 0, activity.dp(4))
                tag = option.value
            }
            radioGroup.addView(rb)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val value = group.findViewById<RadioButton>(checkedId)?.tag as? String ?: return@setOnCheckedChangeListener
            selected.clear()
            selected.add(value)
        }

        wrapper.addView(radioGroup)
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
        }
}