package com.unitx.signal_core.view.dialog

import android.app.Activity
import android.content.res.ColorStateList
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.CheckBox
import com.google.android.material.R
import com.unitx.signal_core.contract.config.dialog.DialogConfig
import com.unitx.signal_core.contract.config.dialog.DialogScope
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.activity.ActivityProvider

internal class DialogCheckboxBinder(
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

        selConfig.options.forEach { option ->
            val cb = CheckBox(themedContext).apply {
                text = option.label
                isChecked = option.value in selected
                buttonTintList = ColorStateList.valueOf(primaryColor)
                setTextColor(contentTextColor)
                setPadding(activity.dp(8), activity.dp(4), 0, activity.dp(4))
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) selected.add(option.value) else selected.remove(option.value)
                }
            }
            container.addView(cb)
        }

        b.dialogPrimaryBtn.setOnClickListener {
            val scope = DialogScope()
            config.positive?.second?.let { scope.it() }
            selConfig.onSelected?.invoke(selected)
            if (config.dismissOnPositive && scope.shouldDismiss) onDismiss()
        }
    }
}