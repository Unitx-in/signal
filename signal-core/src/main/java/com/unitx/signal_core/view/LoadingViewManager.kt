package com.unitx.signal_core.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.widget.ImageViewCompat
import com.unitx.signal_core.contract.config.LoadingConfig
import com.unitx.signal_core.contract.type.LoadingType
import com.unitx.signal_core.databinding.SignalLoadingBinding
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.theme.SignalThemeResolver

internal class LoadingViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: SignalThemeResolver
) {

    private var binding: SignalLoadingBinding? = null
    private var dimOverlay: View? = null

    val container: View?
        get() = binding?.root

    val isShowing: Boolean
        get() = binding?.root?.visibility == View.VISIBLE

    fun attach(config: LoadingConfig, onDismiss: () -> Unit): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.window.decorView.rootView as? ViewGroup ?: return false

        inflateDim(activity, rootView, config.cancelable, onDismiss)
        inflateLoading(activity, rootView, config.horizontalMargin)

        applyTheme(activity)
        bind(config)
        return true
    }

    private fun inflateDim(
        context: Context,
        rootView: ViewGroup,
        cancelable: Boolean,
        onDismiss: () -> Unit
    ) {
        if (dimOverlay != null) return
        dimOverlay = View(context).apply {
            setBackgroundColor(0x99000000.toInt())
            alpha = 0f
            isClickable = true
            isFocusable = true
            animate().alpha(1f).setDuration(220).start()
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            if (cancelable) setOnClickListener { onDismiss() }
        }
        rootView.addView(dimOverlay)
    }

    private fun inflateLoading(context: Context, rootView: ViewGroup, horizontalMargin: Int) {
        if (binding != null) return
        binding = SignalLoadingBinding.inflate(LayoutInflater.from(context), rootView, false)
        rootView.addView(
            binding!!.root,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                val margin = (horizontalMargin * context.resources.displayMetrics.density).toInt()
                leftMargin = margin
                rightMargin = margin
            }
        )
    }

    private fun bind(config: LoadingConfig) {
        val b = binding ?: return

        b.tvTitle.text = config.title
        b.root.contentDescription = config.accessibilityText ?: config.title

        config.icon?.let {
            b.cardIcon.visibility = View.VISIBLE
            b.cardIcon.setImageResource(it)
        } ?: run {
            b.cardIcon.visibility = View.GONE
        }

        updateProgress(config)
    }

    private fun applyTheme(context: Context) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)

        scheme.loadingBackgroundGradient?.let { (start, center, end) ->
            (b.root.background?.mutate() as? GradientDrawable)?.colors =
                intArrayOf(start, center, end)
        }

        scheme.loadingTextColor?.let { color ->
            b.tvTitle.setTextColor(color)
            b.tvSubtitle.setTextColor(color)
        }

        scheme.loadingIconColor?.let { color ->
            ImageViewCompat.setImageTintList(b.cardIcon, ColorStateList.valueOf(color))
        }

        val activeColor = scheme.loadingAnimationActiveColor
        val inactiveColor = scheme.loadingAnimationInactiveColor
        if (activeColor != null && inactiveColor != null) {
            b.loadingDots.applyColors(activeColor, inactiveColor)
        }
    }

    fun updateProgress(config: LoadingConfig) {
        val b = binding ?: return

        when (config.type) {
            LoadingType.Indefinite -> {
                b.tvSubtitle.visibility = if (config.subtitle != null) View.VISIBLE else View.GONE
                b.tvSubtitle.text = config.subtitle
            }
            LoadingType.Determinate -> {
                b.tvSubtitle.visibility = View.VISIBLE
                val percentText = "${config.progress}%"
                b.tvSubtitle.text = if (config.progressMessage != null) {
                    "$percentText · ${config.progressMessage}"
                } else {
                    percentText
                }
            }
        }
    }

    fun release(onReleased: () -> Unit = {}) {
        val rootView = activityProvider.current()?.window?.decorView?.rootView as? ViewGroup
        dimOverlay?.animate()?.alpha(0f)?.setDuration(180)?.withEndAction {
            binding?.root?.let { rootView?.removeView(it) }
            dimOverlay?.let { rootView?.removeView(it) }
            binding = null
            dimOverlay = null
            onReleased()
        }?.start()
    }
}