package com.unitx.signal_core.handler.snackbar

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
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
import com.unitx.signal_core.common.type.SnackPosition
import com.unitx.signal_core.common.config.base.SnackConfig
import com.unitx.signal_core.common.theme.SignalDefaults
import com.unitx.signal_core.common.theme.SignalThemeResolver
import com.unitx.signal_core.common.type.SnackType
import com.unitx.signal_core.databinding.SignalSnackBinding
import com.unitx.signal_core.provider.ActivityProvider

class SnackViewManager(
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

        applyPosition(config.position)
        applyTheme(activity, config.type)
        bind(config, onDismiss)
        return true
    }

    private fun applyPosition(position: SnackPosition) {
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
                SnackPosition.Bottom -> params.bottomMargin = systemBars.bottom
                SnackPosition.Top -> params.topMargin = systemBars.top
            }
            view.layoutParams = params
            insets
        }
        ViewCompat.requestApplyInsets(root)
    }

    private fun applyTheme(context: Context, type: SnackType?) {
        val b = binding ?: return
        val isNight = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val scheme = themeResolver.resolve(context)
        val resolved = SignalDefaults.resolve(scheme, isNight)

        resolved.snackBackground?.let { b.snackContainer.setBackgroundColor(it) }
        resolved.snackTextColor?.let { b.snackText.setTextColor(it) }
        resolved.snackActionTextColor?.let { b.snackActionCustom.setTextColor(it) }
        resolved.snackCancelIconTint?.let {
            b.snackActionCancel.colorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)
        }

        type?.let { type ->
            val backgroundColor = if (isNight) type.backColorDark else type.backColorLight
            if (isNight) b.snackIcon.imageTintList = ContextCompat.getColorStateList(b.root.context, android.R.color.white)
            else b.snackIcon.clearColorFilter()
            b.snackIcon.backgroundTintList = ContextCompat.getColorStateList(b.root.context, backgroundColor)

        } ?: run {
            b.snackIcon.backgroundTintList = null
            b.snackIcon.setBackgroundColor(Color.TRANSPARENT)
        }

//        val iconBackground = resolved.snackIconBackground
//        if (iconBackground != null) {
//            b.snackIcon.backgroundTintList = null
//            b.snackIcon.setBackgroundColor(iconBackground)
//        } else {
//            type?.let { type ->
//                val typeColor = if (isNight) type.backColorDark else type.backColorLight
//                b.snackIcon.backgroundTintList = ContextCompat.getColorStateList(b.root.context, typeColor)
//            } ?: run {
//                b.snackIcon.backgroundTintList = null
//                b.snackIcon.setBackgroundColor(android.graphics.Color.TRANSPARENT)
//            }
//        }
    }

    private fun bind(config: SnackConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.snackText.text = config.message
        b.snackIcon.setImageResource(config.type?.icon ?: SnackType.default.icon)

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

        b.snackActionCancel.visibility = if (config.showCancelAction) View.VISIBLE else View.GONE
        b.snackActionCancel.setOnClickListener { onDismiss() }
    }

    fun release() {
        binding?.snackContainer?.visibility = View.GONE
        val rootView = activityProvider.current()?.window?.decorView?.rootView as? ViewGroup
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
    }
}