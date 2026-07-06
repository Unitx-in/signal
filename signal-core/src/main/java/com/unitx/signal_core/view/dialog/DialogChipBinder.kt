package com.unitx.signal_core.view.dialog

import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.contract.config.dialog.DialogScope
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.activity.ActivityProvider

internal class DialogChipBinder(
    private val activityProvider: ActivityProvider,
    private val primaryColor: Int,
    private val secondaryColor: Int
) {

    fun bind(config: DialogConfig, b: SignalDialogBinding, onDismiss: () -> Unit) {
        val selConfig = config.selection
        val container = b.dialogSelectionContainer

        if (selConfig == null) { container.visibility = View.GONE; return }

        container.removeAllViews()
        container.visibility = View.VISIBLE

        val activity = activityProvider.current() ?: return
        val themedContext = ContextThemeWrapper(
            activity,
            R.style.Theme_MaterialComponents_Light_NoActionBar
        )
        val selected = selConfig.preSelected.toMutableSet()

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
                    intArrayOf(
                        ContextCompat.getColor(activity, android.R.color.white),
                        primaryColor
                    )
                ))
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selected.add(option.value) else selected.remove(option.value)
                }
            }
            chipGroup.addView(chip)
        }

        container.addView(chipGroup)

        b.dialogPrimaryBtn.setOnClickListener {
            val scope = DialogScope()
            config.positive?.second?.let { scope.it() }
            selConfig.onSelected?.invoke(selected)
            if (config.dismissOnPositive && scope.shouldDismiss) onDismiss()
        }
    }
}