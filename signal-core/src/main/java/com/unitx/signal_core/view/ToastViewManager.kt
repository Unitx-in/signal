package com.unitx.signal_core.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import com.unitx.signal_core.R
import com.unitx.signal_core.contract.config.ToastConfig
import com.unitx.signal_core.contract.position.EdgePosition
import com.unitx.signal_core.theme.ThemeResolver
import com.unitx.signal_core.contract.position.IconPosition
import com.unitx.signal_core.contract.position.ToastPosition
import com.unitx.signal_core.databinding.SignalToastBinding
import com.unitx.signal_core.helper.applyInsetPosition
import com.unitx.signal_core.helper.dp
import com.unitx.signal_core.helper.rootViewGroup
import com.unitx.signal_core.provider.ActivityProvider

internal class ToastViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: ThemeResolver
) {

    private var binding: SignalToastBinding? = null

    val container: View?
        get() = binding?.toastContainer

    val isShowing: Boolean
        get() = binding?.toastContainer?.visibility == View.VISIBLE

    fun attach(config: ToastConfig, onDismiss: () -> Unit): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.rootViewGroup() ?: return false

        if (binding == null) {
            val themedContext = ContextThemeWrapper(activity, R.style.SignalTheme)
            binding = SignalToastBinding.inflate(LayoutInflater.from(themedContext), rootView, false)
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

    private fun ToastPosition.toEdge() = when (this) {
        ToastPosition.Top -> EdgePosition.Top
        ToastPosition.Bottom -> EdgePosition.Bottom
        ToastPosition.Center -> EdgePosition.Center
    }

    private fun applyTheme(context: Context, config: ToastConfig) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)
        val type = config.type

        @ColorInt val backgroundColor = scheme.toastBackground
            ?: ContextCompat.getColor(context, type.backgroundColor)
        @ColorInt val strokeColor = scheme.toastStrokeColor
            ?: ContextCompat.getColor(context, type.foregroundColor)
        @ColorInt val textColor = scheme.toastTextColor
            ?: ContextCompat.getColor(context, type.foregroundColor)
        @ColorInt val iconColor = scheme.toastIconColor
            ?: ContextCompat.getColor(context, type.foregroundColor)

        b.toastContainer.setCardBackgroundColor(backgroundColor)
        b.toastContainer.strokeWidth = context.dp(2)
        b.toastContainer.strokeColor = strokeColor
        b.toastText.setTextColor(textColor)
        b.toastText.compoundDrawables.forEach { it?.setTint(iconColor) }
    }

    private fun bind(config: ToastConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.toastText.text = config.message
        b.toastContainer.contentDescription = config.accessibilityText ?: config.message

        val icon: Drawable? = config.iconRes?.let {
            ContextCompat.getDrawable(b.root.context, it)?.apply {
                val size = b.root.context.dp(18)
                setBounds(0, 0, size, size)
            }
        }

        b.toastText.setCompoundDrawables(
            if (config.iconPosition == IconPosition.Start) icon else null,
            if (config.iconPosition == IconPosition.Top) icon else null,
            if (config.iconPosition == IconPosition.End) icon else null,
            if (config.iconPosition == IconPosition.Bottom) icon else null
        )
        b.toastText.compoundDrawablePadding = if (icon != null) b.root.context.dp(8) else 0
    }

    fun release() {
        binding?.toastContainer?.visibility = View.GONE
        val rootView = activityProvider.current()?.rootViewGroup()
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
    }
}