package com.unitx.signal_core.handler

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import com.unitx.signal_core.common.SignalQueue
import com.unitx.signal_core.common.SnackPosition
import com.unitx.signal_core.common.config.SnackConfig
import com.unitx.signal_core.core.SignalCore
import com.unitx.signal_core.databinding.SignalSnackBinding

class SnackHandler(
    private val host: SignalCore,
    private val defaultConfig: SnackConfig,
    private val queue: SignalQueue
) {

    companion object {
        private const val SNACK_DURATION = 2500L
        private const val SNACK_DISMISS_DELAY = 300L // animation out duration
    }

    private var binding: SignalSnackBinding? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private var dismissRunnable: Runnable? = null

    val isShowing: Boolean
        get() = binding?.sLlSnackContainer?.visibility == View.VISIBLE

    // --- public API ---

    fun show(message: String) = show(message) {}

    fun show(message: String, block: SnackConfig.() -> Unit) {
        android.util.Log.d("Signal", "show() called with: $message")

        val config = SnackConfig().apply {
            type = defaultConfig.type
            duration = defaultConfig.duration
            dismissOnBackPress = defaultConfig.dismissOnBackPress
            anchorViewId = defaultConfig.anchorViewId
            position = defaultConfig.position
        }.apply(block)
        config.message = message

        queue.enqueue(
            show = { display(config) },
            dismiss = { dismiss() },
            isShowing = { isShowing }
        )
    }

    // --- internal ---

    private fun display(config: SnackConfig) {
        val activity = host.current() ?: return
        val rootView = activity.window.decorView.rootView as? ViewGroup ?: return

        if (binding == null) {
            binding = SignalSnackBinding.inflate(
                LayoutInflater.from(activity),
                rootView,
                false
            )

            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = when (config.position) {
                    SnackPosition.Bottom -> Gravity.BOTTOM
                    SnackPosition.Top -> Gravity.TOP
                }
            }

            rootView.addView(binding!!.root, params)

            ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                val layoutParams = view.layoutParams as FrameLayout.LayoutParams
                when (config.position) {
                    SnackPosition.Bottom -> layoutParams.bottomMargin = systemBars.bottom
                    SnackPosition.Top -> layoutParams.topMargin = systemBars.top
                }
                view.layoutParams = layoutParams
                insets
            }
            ViewCompat.requestApplyInsets(binding!!.root)
        }

        binding!!.apply {
            sSnackText.text = config.message

            config.type?.let { type ->
                sSnackIcon.setImageResource(type.icon)
                sSnackIcon.backgroundTintList =
                    ContextCompat.getColorStateList(root.context, type.backColor)
            }

            sSnackActionCancel.setOnClickListener {
                cancelDismiss()
                dismiss()
            }

            slideIn(config.position)
            scheduleDismiss()
        }
    }

    private fun slideIn(position: SnackPosition) {
        val container = binding?.sLlSnackContainer ?: return
        container.visibility = View.VISIBLE
        container.doOnLayout {
            val startY = when (position) {
                SnackPosition.Bottom -> container.height.toFloat()
                SnackPosition.Top -> -container.height.toFloat()
            }
            container.translationY = startY
            container.animate()
                .translationY(0f)
                .setDuration(300)
                .start()
        }
    }

    private fun slideOut(position: SnackPosition, onEnd: () -> Unit) {
        val container = binding?.sLlSnackContainer ?: return
        val endY = when (position) {
            SnackPosition.Bottom -> container.height.toFloat()
            SnackPosition.Top -> -container.height.toFloat()
        }
        container.animate()
            .translationY(endY)
            .setDuration(250)
            .withEndAction {
                container.visibility = View.GONE
                onEnd()
            }
            .start()
    }

    private fun scheduleDismiss() {
        cancelDismiss()
        dismissRunnable = Runnable { dismiss() }
        mainHandler.postDelayed(dismissRunnable!!, SNACK_DURATION)
    }

    private fun cancelDismiss() {
        dismissRunnable?.let { mainHandler.removeCallbacks(it) }
        dismissRunnable = null
    }

    fun dismiss() {
        cancelDismiss()
        slideOut(defaultConfig.position) {
            queue.next()
        }
    }

    fun release() {
        cancelDismiss()
        binding?.sLlSnackContainer?.visibility = View.GONE
        val rootView = host.current()?.window?.decorView?.rootView as? ViewGroup
        binding?.root?.let { rootView?.removeView(it) }
        binding = null
    }
}