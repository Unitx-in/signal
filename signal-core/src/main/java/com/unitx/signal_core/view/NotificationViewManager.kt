package com.unitx.signal_core.view

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.unitx.signal_core.contract.config.NotificationConfig
import com.unitx.signal_core.contract.position.EdgePosition
import com.unitx.signal_core.theme.ThemeResolver
import com.unitx.signal_core.databinding.SignalNotificationBinding
import com.unitx.signal_core.helper.applyInsetPosition
import com.unitx.signal_core.helper.rootViewGroup
import com.unitx.signal_core.contract.position.NotificationPosition
import com.unitx.signal_core.helper.SignalImageLoader

internal class NotificationViewManager(
    private val themeResolver: ThemeResolver
) {

    private var binding: SignalNotificationBinding? = null
    private var attachedActivity: Activity? = null

    val container: View?
        get() = binding?.signalNotifCard

    val isShowing: Boolean
        get() = binding?.signalNotifCard?.visibility == View.VISIBLE

    fun attach(activity: Activity, config: NotificationConfig, onDismiss: () -> Unit): Boolean {
        val rootView = activity.rootViewGroup() ?: return false
        attachedActivity = activity

        if (binding == null) {
            binding = SignalNotificationBinding.inflate(LayoutInflater.from(activity), rootView, false)
            rootView.addView(
                binding!!.root, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

        applyInsetPosition(
            root = binding!!.root,
            position = config.position.toEdge(),
            topOffset = config.topOffset,
            bottomOffset = config.bottomOffset,
            centerHorizontal = true
        )
        bind(config, onDismiss)
        applyTheme(activity, config)
        return true
    }


    private fun NotificationPosition.toEdge() = when (this) {
        NotificationPosition.Top -> EdgePosition.Top
        NotificationPosition.Bottom -> EdgePosition.Bottom
        NotificationPosition.Center -> EdgePosition.Center
    }

    private fun bind(config: NotificationConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.signalNotifTextSimple.text = config.message
        b.signalNotifTextHighlight.text = config.highlight
        b.signalNotifCard.contentDescription =
            config.accessibilityText ?: "${config.message} ${config.highlight}"

        when {
            config.iconRes != null -> {
                b.signalNotifIcon.setImageResource(config.iconRes!!)
            }
            config.iconUrl != null -> {
                b.signalNotifIcon.setImageDrawable(null)
                SignalImageLoader.load(config.iconUrl!!, b.signalNotifIcon, requestTag = config.iconUrl!!)
            }
            else -> {
                b.signalNotifIcon.setImageDrawable(null)
            }
        }
    }

    private fun applyTheme(context: Context, config: NotificationConfig) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)

        scheme.notifBackground?.let { b.signalNotifCard.setCardBackgroundColor(it) }
        scheme.notifTextColor?.let { b.signalNotifTextSimple.setTextColor(it) }
        scheme.notifHighlightTextColor?.let { b.signalNotifTextHighlight.setTextColor(it) }
    }

    fun release() {
        binding?.signalNotifCard?.visibility = View.GONE
        val rootView = attachedActivity?.rootViewGroup()
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
        attachedActivity = null
    }
}