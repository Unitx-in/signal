//package com.unitx.signal_core.handler
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.os.Handler
//import android.os.Looper
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import com.unitx.signal_core.R
//import com.unitx.signal_core.common.SignalHandler
//import com.unitx.signal_core.feature.SignalQueue
//import com.unitx.signal_core.common.config.base.BannerConfig
//import com.unitx.signal_core.core.SignalHost
//
//class BannerHandler(
//    private val host: SignalHost,
//    private val defaultConfig: BannerConfig,
//    private val queue: SignalQueue
//) : SignalHandler {
//
//    private var currentBanner: View? = null
//    private val mainHandler = Handler(Looper.getMainLooper())
//    private var dismissRunnable: Runnable? = null
//
//    override val isShowing: Boolean
//        get() = currentBanner?.visibility == View.VISIBLE
//
//    fun show(message: String) = show(message) {}
//
//    fun show(message: String, block: BannerConfig.() -> Unit) {
//        val config = BannerConfig().apply {
//            type = defaultConfig.type
//            autoDismiss = defaultConfig.autoDismiss
//            autoDismissDelay = defaultConfig.autoDismissDelay
//            dismissOnBackPress = defaultConfig.dismissOnBackPress
//            position = defaultConfig.position
//        }.apply(block)
//
//        queue.enqueue(object : SignalHandler {
//            override val isShowing: Boolean get() = this@BannerHandler.isShowing
//            override fun show() = display(message, config)
//            override fun dismiss() = this@BannerHandler.dismiss()
//        })
//    }
//
//    private fun display(message: String, config: BannerConfig) {
//        val activity = host.current() ?: return
//        val rootView = activity.window.decorView.rootView as? ConstraintLayout ?: return
//
//        dismiss() // clear any existing banner
//
//        val banner = LayoutInflater.from(activity)
//            .inflate(R.layout.signal_banner, rootView, false)
//
//        banner.findViewById<TextView>(R.id.signal_banner_message).text = message
//        config.action?.let { (label, block) ->
//            banner.findViewById<TextView>(R.id.signal_banner_action).apply {
//                visibility = View.VISIBLE
//                text = label
//                setOnClickListener { block(); dismiss() }
//            }
//        }
//
//        // position
//        val params = banner.layoutParams as? ConstraintLayout.LayoutParams
//        if (config.position == BannerPosition.Bottom) {
//            params?.topToTop = ConstraintLayout.LayoutParams.UNSET
//            params?.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
//        } else {
//            params?.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
//            params?.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
//        }
//        banner.layoutParams = params
//
//        rootView.addView(banner)
//        currentBanner = banner
//
//        // slide in
//        banner.translationY = if (config.position == BannerPosition.Top)
//            -banner.height.toFloat() else banner.height.toFloat()
//        banner.animate().translationY(0f).setDuration(300).start()
//
//        // auto dismiss
//        if (config.autoDismiss) {
//            dismissRunnable = Runnable { dismiss() }
//            mainHandler.postDelayed(dismissRunnable!!, config.autoDismissDelay)
//        }
//    }
//
//    override fun show() {}
//
//    override fun dismiss() {
//        dismissRunnable?.let { mainHandler.removeCallbacks(it) }
//        dismissRunnable = null
//
//        currentBanner?.animate()
//            ?.translationY(-currentBanner!!.height.toFloat())
//            ?.setDuration(250)
//            ?.setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    (currentBanner?.parent as? ConstraintLayout)?.removeView(currentBanner)
//                    currentBanner = null
//                    queue.next()
//                }
//            })?.start()
//    }
//}