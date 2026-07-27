package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.unitx.signal_core.contract.config.dialog.DialogSelectionConfig
import com.unitx.signal_core.helper.dp

internal class DialogChipBinder(
    private val primaryColor: Int,
    private val secondaryColor: Int,
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

        val chipGroup = ChipGroup(themedContext).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            isSingleSelection = false
        }

        selConfig.options.forEach { option ->
            val chip = Chip(themedContext).apply {
                text = option.label
                isCheckable = true
                isChecked = option.value in selected
                chipBackgroundColor = ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(primaryColor, secondaryColor)
                )
                setTextColor(ColorStateList(
                    arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                    intArrayOf(ContextCompat.getColor(activity, android.R.color.white), primaryColor)
                ))
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selected.add(option.value) else selected.remove(option.value)
                }
            }
            chipGroup.addView(chip)
        }

        wrapper.addView(chipGroup)
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