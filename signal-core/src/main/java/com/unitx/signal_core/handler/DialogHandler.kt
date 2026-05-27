package com.unitx.signal_core.handler

import android.app.AlertDialog
import com.unitx.signal_core.common.SignalHandler
import com.unitx.signal_core.common.config.DialogConfig
import com.unitx.signal_core.core.SignalCore

class DialogHandler(
    private val host: SignalCore,
    private val defaultConfig: DialogConfig
) : SignalHandler {

    private var currentDialog: AlertDialog? = null

    override val isShowing: Boolean
        get() = currentDialog?.isShowing == true

    fun show(block: DialogConfig.() -> Unit) {
        val config = DialogConfig().apply {
            type = defaultConfig.type
            cancelable = defaultConfig.cancelable
        }.apply(block)

        val activity = host.current() ?: return

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
                // dismiss safely if activity is finishing
                if (!activity.isFinishing && !activity.isDestroyed) {
                    dialog.show()
                }
            }
    }

    override fun show() {}

    override fun dismiss() {
        currentDialog?.dismiss()
        currentDialog = null
    }
}