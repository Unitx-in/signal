package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.R
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.contract.config.dialog.DialogScope
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.activity.ActivityProvider

internal class DialogRadioBinder(
    private val primaryColor: Int,
    private val contentTextColor: Int
) {

    fun bind(activity: Activity, config: DialogConfig, b: SignalDialogBinding, onDismiss: () -> Unit) {
        val selConfig = config.selection
        val container = b.dialogSelectionContainer

        if (selConfig == null) { container.visibility = View.GONE; return }

        container.removeAllViews()
        container.visibility = View.VISIBLE

        val themedContext = ContextThemeWrapper(
            activity,
            R.style.Theme_MaterialComponents_Light_NoActionBar
        )
        val selected = selConfig.preSelected.toMutableSet()

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

        container.addView(radioGroup)

        b.dialogPrimaryBtn.setOnClickListener {
            val scope = DialogScope()
            config.positive?.second?.let { scope.it() }
            selConfig.onSelected?.invoke(selected)
            if (config.dismissOnPositive && scope.shouldDismiss) onDismiss()
        }
    }
}