package com.unitx.signal_core.handler.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.unitx.signal_core.R
import com.unitx.signal_core.common.config.base.DialogConfig
import com.unitx.signal_core.common.theme.SignalThemeResolver
import com.unitx.signal_core.databinding.SignalDialogBinding
import com.unitx.signal_core.provider.ActivityProvider

class DialogViewManager(
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

        val headerColor = scheme.dialogSecondaryColor ?: ContextCompat.getColor(context, type.secondaryColor)
        val headingColor = scheme.dialogPrimaryColor ?: ContextCompat.getColor(context, type.primaryColor)

        val titleColor = scheme.dialogTextColor ?: ContextCompat.getColor(context, R.color.signalBlack)
        val messageColor = scheme.dialogTextColor ?: ContextCompat.getColor(context, R.color.signalBlack)
        val iconColor = scheme.dialogPrimaryColor ?: ContextCompat.getColor(context, type.primaryColor)

        val bgColor = scheme.dialogBackground ?: ContextCompat.getColor(context, android.R.color.white)
        val closeButtonColor = scheme.dialogPrimaryColor ?: ContextCompat.getColor(context, type.primaryColor)

        val primaryBtnBackColor = scheme.dialogPrimaryColor ?: ContextCompat.getColor(context, type.primaryColor)
        val primaryBtnTextColor = scheme.dialogPrimaryButtonTextColor ?: ContextCompat.getColor(context, android.R.color.white)
        val secondaryBtnTextColor = scheme.dialogPrimaryColor ?: ContextCompat.getColor(context, type.primaryColor)

        b.dialogCard.setCardBackgroundColor(bgColor)
        b.dialogHeaderBg.setBackgroundColor(headerColor)
        b.dialogHeaderLabel.setTextColor(headingColor)
        b.dialogClose.imageTintList = ColorStateList.valueOf(closeButtonColor)
        b.dialogIcon.imageTintList = ColorStateList.valueOf(iconColor)
        b.dialogTitle.setTextColor(titleColor)
        b.dialogMessage.setTextColor(messageColor)
        (b.dialogPrimaryBtn.background as? GradientDrawable)?.setColor(primaryBtnBackColor)
        b.dialogPrimaryBtn.setTextColor(primaryBtnTextColor)
        (b.dialogSecondaryBtn.background as? GradientDrawable)?.setStroke(1, secondaryBtnTextColor)
        b.dialogSecondaryBtn.setTextColor(secondaryBtnTextColor)
    }

    private fun bind(config: DialogConfig, onDismiss: () -> Unit) {
        val b = binding ?: return

        b.dialogTitle.text = config.title
        b.dialogMessage.text = config.message
        b.dialogClose.setOnClickListener { onDismiss() }
        b.dialogIcon.setImageResource(config.icon ?: config.type.icon)

        config.header.takeIf { it.isNotBlank() }?.let { h->
            b.dialogHeaderLabel.text = h
        } ?: run {
            b.dialogHeaderLabel.text = ContextCompat.getString(b.root.context, config.type.header)
        }


        config.positive?.let { (label, block) ->
            b.dialogPrimaryBtn.visibility = View.VISIBLE
            b.dialogPrimaryBtn.text = label
            b.dialogPrimaryBtn.setOnClickListener { block(); onDismiss() }
        } ?: run {
            b.dialogPrimaryBtn.visibility = View.GONE
        }

        config.negative?.let { (label, block) ->
            b.dialogSecondaryBtn.visibility = View.VISIBLE
            b.dialogSecondaryBtn.text = label
            b.dialogSecondaryBtn.setOnClickListener { block(); onDismiss() }
        } ?: run {
            b.dialogSecondaryBtn.visibility = View.GONE
        }
    }

    fun release() {
        val rootView = activityProvider.current()?.window?.decorView?.rootView as? ViewGroup
        dimOverlay?.animate()?.alpha(0f)?.setDuration(180)?.withEndAction {
            binding?.root?.let { rootView?.removeView(it) }
            dimOverlay?.let { rootView?.removeView(it) }
            binding = null
            dimOverlay = null
        }?.start()
    }
}