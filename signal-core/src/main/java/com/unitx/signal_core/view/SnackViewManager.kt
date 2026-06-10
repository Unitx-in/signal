package com.unitx.signal_core.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.unitx.signal_core.contract.config.SnackConfig
import com.unitx.signal_core.theme.SignalThemeResolver
import com.unitx.signal_core.contract.position.SnackPosition
import com.unitx.signal_core.contract.type.SnackType
import com.unitx.signal_core.databinding.SignalSnackBinding
import com.unitx.signal_core.provider.ActivityProvider

internal class SnackViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: SignalThemeResolver
) {

    private var binding: SignalSnackBinding? = null

    val container: View?
        get() = binding?.snackContainer

    val isShowing: Boolean
        get() = binding?.snackContainer?.visibility == View.VISIBLE

    fun attach(config: SnackConfig, onDismiss: () -> Unit): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.window.decorView.rootView as? ViewGroup ?: return false

        if (binding == null) {
            binding = SignalSnackBinding.inflate(
                LayoutInflater.from(activity),
                rootView,
                false
            )
            rootView.addView(binding!!.root, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ))
        }

        applyPosition(config.position, config.topOffset, config.bottomOffset)
        applyTheme(activity, config.type)
        bind(config, onDismiss)
        return true
    }

    private fun applyPosition(position: SnackPosition, topOffset: Int, bottomOffset: Int) {
        val root = binding?.root ?: return
        val layoutParams = root.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = when (position) {
            SnackPosition.Bottom -> Gravity.BOTTOM
            SnackPosition.Top -> Gravity.TOP
        }
        root.layoutParams = layoutParams

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.topMargin = 0
            params.bottomMargin = 0
            when (position) {
                SnackPosition.Bottom -> params.bottomMargin = systemBars.bottom + bottomOffset
                SnackPosition.Top -> params.topMargin = systemBars.top + topOffset
            }
            view.layoutParams = params
            insets
        }
        ViewCompat.requestApplyInsets(root)
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

        config.action?.let { (label, block) ->
            b.snackActionCustom.visibility = View.VISIBLE
            b.snackActionCustom.text = label
            b.snackActionCustom.setOnClickListener {
                block()
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
        val rootView = activityProvider.current()?.window?.decorView?.rootView as? ViewGroup
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
    }
}