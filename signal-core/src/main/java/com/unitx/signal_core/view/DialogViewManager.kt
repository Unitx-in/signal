package com.unitx.signal_core.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.unitx.signal_core.R
import com.unitx.signal_core.contract.config.DialogConfig
import com.unitx.signal_core.theme.SignalThemeResolver
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.provider.ActivityProvider

internal class DialogViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: SignalThemeResolver
) {

    private var binding: SignalDialogBinding? = null
    private var dimOverlay: View? = null

    val container: View?
        get() = binding?.dialogCard

    val isShowing: Boolean
        get() = binding?.dialogCard?.visibility == View.VISIBLE

    fun attach(config: DialogConfig, onDismiss: () -> Unit): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.window.decorView.rootView as? ViewGroup ?: return false

        inflateDim(activity, rootView, config.cancelable, onDismiss)
        inflateDialog(activity, rootView, config.horizontalMargin)

        applyTheme(activity, config)
        bind(config, onDismiss)
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

    private fun inflateDialog(context: Context, rootView: ViewGroup, horizontalMargin: Int) {
        if (binding != null) return
        binding = SignalDialogBinding.inflate(LayoutInflater.from(context), rootView, false)
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

    private fun applyTheme(context: Context, config: DialogConfig) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)
        val type = config.type

        fun color(@ColorRes res: Int) = ContextCompat.getColor(context, res)
        val primary = scheme.dialogPrimaryColor ?: color(type.primaryColor)
        val secondary = scheme.dialogSecondaryColor ?: color(type.secondaryColor)
        val textColor = scheme.dialogTextColor ?: color(R.color.signalBlack)
        val primaryBtnTextColor = scheme.dialogPrimaryButtonTextColor ?: color(android.R.color.white)

        b.dialogCard.setCardBackgroundColor(scheme.dialogBackground ?: color(android.R.color.white))
        b.dialogHeaderBg.setBackgroundColor(secondary)
        b.dialogHeaderLabel.setTextColor(primary)
        b.dialogClose.imageTintList = ColorStateList.valueOf(primary)
        b.dialogIcon.imageTintList = ColorStateList.valueOf(primary)
        b.dialogTitle.setTextColor(textColor)
        b.dialogMessage.setTextColor(textColor)
        (b.dialogPrimaryBtn.background as? GradientDrawable)?.setColor(primary)
        b.dialogPrimaryBtn.setTextColor(primaryBtnTextColor)
        (b.dialogSecondaryBtn.background as? GradientDrawable)?.setStroke(1, primary)
        b.dialogSecondaryBtn.setTextColor(primary)
        b.dialogNeutralText.setTextColor(primary)
    }

    private fun bind(config: DialogConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.dialogTitle.text = config.title
        b.dialogCard.contentDescription = config.accessibilityText ?: "${config.title}. ${config.message}"
        b.dialogMessage.text = config.message
        b.dialogClose.setOnClickListener { onDismiss() }
        b.dialogIcon.setImageResource(config.icon ?: config.type.icon)

        config.header.takeIf { it.isNotBlank() }?.let { h->
            b.dialogHeaderLabel.text = h
        } ?: run {
            b.dialogHeaderLabel.text = ContextCompat.getString(b.root.context, config.type.header)
        }


        config.positive?.let { (label, onClick) ->
            b.dialogPrimaryBtn.visibility = View.VISIBLE
            b.dialogPrimaryBtn.text = label
            b.dialogPrimaryBtn.setOnClickListener {
                onClick()
                if (config.dismissOnPositive) onDismiss()
            }
        } ?: run {
            b.dialogPrimaryBtn.visibility = View.GONE
        }

        config.negative?.let { (label, onClick) ->
            b.dialogSecondaryBtn.visibility = View.VISIBLE
            b.dialogSecondaryBtn.text = label
            b.dialogSecondaryBtn.setOnClickListener {
                onClick()
                if (config.dismissOnNegative) onDismiss()
            }
        } ?: run {
            b.dialogSecondaryBtn.visibility = View.GONE
        }

        config.neutral?.let { (label , onClick) ->
            b.dialogNeutralText.visibility = View.VISIBLE
            b.dialogNeutralText.text = label
            b.dialogNeutralText.setOnClickListener {
                onClick()
                if (config.dismissOnNeutral) onDismiss()
            }
        } ?: run {
            b.dialogNeutralText.visibility = View.GONE
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