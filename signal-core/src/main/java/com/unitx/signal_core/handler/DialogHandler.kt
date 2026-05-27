package com.unitx.signal_core.handler

import android.app.AlertDialog
import com.unitx.signal_core.common.config.base.DialogConfig
import com.unitx.signal_core.provider.ActivityProvider

class DialogHandler(
    private val activityProvider: ActivityProvider,
    private val defaultConfig: DialogConfig
) {

    private var currentDialog: AlertDialog? = null

    val isShowing: Boolean
        get() = currentDialog?.isShowing == true

    fun show(block: DialogConfig.() -> Unit) {
        val config = DialogConfig().apply(block)
        val activity = activityProvider.current() ?: return

        currentDialog?.dismiss()
        currentDialog = AlertDialog.Builder(activity)
            .apply {
                if (config.title.isNotBlank()) setTitle(config.title)
                if (config.message.isNotBlank()) setMessage(config.message)
                setCancelable(config.cancelable)
                config.positive?.let { (label, block) ->
                    setPositiveButton(label) { _, _ -> block() }
                }
                config.negative?.let { (label, block) ->
                    setNegativeButton(label) { _, _ -> block() }
                }
                config.neutral?.let { (label, block) ->
                    setNeutralButton(label) { _, _ -> block() }
                }
            }
            .create()
            .also { dialog ->
                if (!activity.isFinishing && !activity.isDestroyed) {
                    dialog.show()
                }
            }
    }

    fun dismiss() {
        currentDialog?.dismiss()
        currentDialog = null
    }
}