package com.unitx.signal_core.handler.snack

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
import com.unitx.signal_core.databinding.SignalSnackBinding
import com.unitx.signal_core.provider.ActivityProvider

class SnackViewManager(
    private val activityProvider: ActivityProvider
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

    private fun bind(config: SnackConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.snackText.text = config.message

        config.type?.let { type ->
            b.snackIcon.setImageResource(type.icon)
            b.snackIcon.backgroundTintList =
                ContextCompat.getColorStateList(b.root.context, type.backColor)
        }

        config.action?.let { (label, block) ->
            b.snackActionCuston.visibility = View.VISIBLE
            b.snackActionCuston.text = label
            b.snackActionCuston.setOnClickListener {
                block()
                onDismiss()
            }
        } ?: run {
            b.snackActionCuston.visibility = View.GONE
            b.snackActionCuston.setOnClickListener(null)
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