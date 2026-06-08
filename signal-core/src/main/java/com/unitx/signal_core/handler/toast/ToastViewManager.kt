package com.unitx.signal_core.handler.toast

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unitx.signal_core.R
import com.unitx.signal_core.common.config.base.ToastConfig
import com.unitx.signal_core.common.theme.SignalThemeResolver
import com.unitx.signal_core.common.type.IconPosition
import com.unitx.signal_core.common.type.ToastPosition
import com.unitx.signal_core.databinding.SignalToastBinding
import com.unitx.signal_core.provider.ActivityProvider

class ToastViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: SignalThemeResolver
) {

    private var binding: SignalToastBinding? = null

    val container: View?
        get() = binding?.toastContainer

    val isShowing: Boolean
        get() = binding?.toastContainer?.visibility == View.VISIBLE

    fun attach(config: ToastConfig, message: String): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.window.decorView.rootView as? ViewGroup ?: return false

        if (binding == null) {
            val themedContext = ContextThemeWrapper(activity, R.style.SignalTheme)
            binding = SignalToastBinding.inflate(
                LayoutInflater.from(themedContext),
                rootView,
                false
            )
            rootView.addView(
                binding!!.root, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }

        applyPosition(config.position)
        bind(message, config)
        applyTheme(activity, config)

        return true
    }

    private fun applyPosition(position: ToastPosition) {
        val root = binding?.root ?: return
        val layoutParams = root.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL or when (position) {
            ToastPosition.Bottom -> Gravity.BOTTOM
            ToastPosition.Top -> Gravity.TOP
            ToastPosition.Center -> Gravity.CENTER_VERTICAL
        }
        root.layoutParams = layoutParams

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.topMargin = 0
            params.bottomMargin = 0
            when (position) {
                ToastPosition.Bottom -> params.bottomMargin = systemBars.bottom + dpToPx(view.context, 24)
                ToastPosition.Top -> params.topMargin = systemBars.top + dpToPx(view.context, 24)
                ToastPosition.Center -> {}
            }
            view.layoutParams = params
            insets
        }
        ViewCompat.requestApplyInsets(root)
    }

    private fun applyTheme(context: Context, config: ToastConfig) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)
        val type = config.type

        @ColorInt
        val backgroundColor = scheme.toastBackground
            ?: ContextCompat.getColor(context, type.backgroundColor)

        @ColorInt
        val strokeColor = scheme.toastStrokeColor
            ?: ContextCompat.getColor(context, type.foregroundColor)

        @ColorInt
        val textColor = scheme.toastTextColor
            ?: ContextCompat.getColor(context, type.foregroundColor)

        @ColorInt
        val iconColor = scheme.toastIconColor
            ?: ContextCompat.getColor(context, type.foregroundColor)

        b.toastContainer.setCardBackgroundColor(backgroundColor)
        b.toastContainer.strokeWidth = dpToPx(context, 2)
        b.toastContainer.strokeColor = strokeColor

        b.toastText.setTextColor(textColor)

        b.toastText.compoundDrawables.forEach { drawable ->
            drawable?.setTint(iconColor)
        }
    }

    private fun bind(message: String, config: ToastConfig) {
        val b = binding ?: return

        b.toastText.text = message

        val icon: Drawable? = config.iconRes?.let {
            ContextCompat.getDrawable(b.root.context, it)?.apply {
                val size = dpToPx(b.root.context, 18)
                setBounds(0, 0, size, size)
            }
        }

        b.toastText.setCompoundDrawables(
            if (config.iconPosition == IconPosition.Start) icon else null,
            if (config.iconPosition == IconPosition.Top) icon else null,
            if (config.iconPosition == IconPosition.End) icon else null,
            if (config.iconPosition == IconPosition.Bottom) icon else null
        )
        b.toastText.compoundDrawablePadding = if (icon != null) dpToPx(b.root.context, 8) else 0
    }

    fun release() {
        binding?.toastContainer?.visibility = View.GONE
        val rootView = activityProvider.current()?.window?.decorView?.rootView as? ViewGroup
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
    }

    private fun dpToPx(context: Context, dp: Int): Int =
        (dp * context.resources.displayMetrics.density).toInt()
}