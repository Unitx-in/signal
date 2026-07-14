package com.unitx.signal_core.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.unitx.signal_core.contract.config.SnackConfig
import com.unitx.signal_core.contract.position.EdgePosition
import com.unitx.signal_core.theme.ThemeResolver
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType
import com.unitx.signal_core.databinding.SignalSnackBinding
import com.unitx.signal_core.helper.applyInsetPosition
import com.unitx.signal_core.helper.rootViewGroup
import com.unitx.signal_core.activity.ActivityProvider

internal class SnackViewManager(
    private val themeResolver: ThemeResolver
) {

    private var binding: SignalSnackBinding? = null
    private var attachedActivity: Activity? = null

    val container: View?
        get() = binding?.snackContainer

    val isShowing: Boolean
        get() = binding?.snackContainer?.visibility == View.VISIBLE

    fun attach(activity: Activity, config: SnackConfig, onDismiss: () -> Unit): Boolean {
        val rootView = activity.rootViewGroup() ?: return false
        attachedActivity = activity

        if (binding == null) {
            binding = SignalSnackBinding.inflate(LayoutInflater.from(activity), rootView, false)
            rootView.addView(
                binding!!.root, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

        applyInsetPosition(
            root = binding!!.root,
            position = config.position.toEdge(),
            topOffset = config.topOffset,
            bottomOffset = config.bottomOffset
        )
        applyTheme(activity, config.type)
        bind(config, onDismiss)
        return true
    }

    private fun SnackPosition.toEdge() = when (this) {
        SnackPosition.Top -> EdgePosition.Top
        SnackPosition.Bottom -> EdgePosition.Bottom
    }

    private fun applyTheme(context: Context, type: SnackType) {
        val b = binding ?: return
        val isNight = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val scheme = themeResolver.resolve(context)

        scheme.snackBackground?.let { b.snackContainer.setBackgroundColor(it) }
        scheme.snackTextColor?.let { b.snackText.setTextColor(it) }
        scheme.snackActionTextColor?.let { b.snackActionCustom.setTextColor(it) }
        scheme.snackCancelIconTint?.let {
            b.snackActionCancel.colorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)
        }

        val iconBackgroundColor = if (isNight) type.iconBackDark else type.iconBackLight
        if (isNight) b.snackIcon.imageTintList = ContextCompat.getColorStateList(b.root.context, android.R.color.white)
        else b.snackIcon.clearColorFilter()
        b.snackIcon.backgroundTintList = ContextCompat.getColorStateList(b.root.context, iconBackgroundColor)
    }

    private fun bind(config: SnackConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.snackText.text = config.message
        b.snackContainer.contentDescription = config.accessibilityText ?: config.message
        b.snackIcon.setImageResource(config.type.icon)

        config.action?.let { (label, onClick) ->
            b.snackActionCustom.visibility = View.VISIBLE
            b.snackActionCustom.text = label
            b.snackActionCustom.setOnClickListener {
                onClick()
                onDismiss()
            }
        } ?: run {
            b.snackActionCustom.visibility = View.GONE
            b.snackActionCustom.setOnClickListener(null)
        }

        val showCancel = config.persistent || config.showCancelAction
        b.snackActionCancel.visibility = if (showCancel) View.VISIBLE else View.GONE
        b.snackActionCancel.setOnClickListener { onDismiss() }
    }

    fun release() {
        binding?.snackContainer?.visibility = View.GONE
        val rootView = attachedActivity?.rootViewGroup()
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
        attachedActivity = null
    }
}