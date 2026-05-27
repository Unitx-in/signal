//package com.unitx.signal_core.handler
//
//import android.app.Dialog
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.view.LayoutInflater
//import android.view.Window
//import android.widget.TextView
//import androidx.fragment.app.FragmentActivity
//import com.unitx.signal_core.R
//import com.unitx.signal_core.common.type.LoadingAnimation
//import com.unitx.signal_core.common.SignalHandler
//import com.unitx.signal_core.common.config.base.LoadingConfig
//import com.unitx.signal_core.core.SignalHost
//
//class LoadingHandler(
//    private val host: SignalHost,
//    private val defaultConfig: LoadingConfig
//) : SignalHandler {
//
//    private var currentDialog: Dialog? = null
//
//    override val isShowing: Boolean
//        get() = currentDialog?.isShowing == true
//
//    fun show() = show(defaultConfig.message)
//
//    fun show(message: String) = show(message) {}
//
//    fun show(message: String, block: LoadingConfig.() -> Unit) {
//        val config = LoadingConfig().apply {
//            this.message = defaultConfig.message
//            cancelable = defaultConfig.cancelable
//            animation = defaultConfig.animation
//        }.apply(block)
//        config.message = message
//
//        val activity = host.current() ?: return
//        if (isShowing) {
//            // just update message if already showing
//            currentDialog?.findViewById<TextView>(R.id.signal_loading_message)
//                ?.text = message
//            return
//        }
//
//        currentDialog = buildDialog(activity, config).also { it.show() }
//    }
//
//    private fun buildDialog(activity: FragmentActivity, config: LoadingConfig): Dialog {
//        return Dialog(activity).apply {
//            requestWindowFeature(Window.FEATURE_NO_TITLE)
//            setCancelable(config.cancelable)
//            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//            val view = LayoutInflater.from(activity)
//                .inflate(R.layout.signal_loading_dialog, null)
//
//            view.findViewById<TextView>(R.id.signal_loading_message).text = config.message
//
//            // handle animation type
//            when (val anim = config.animation) {
//                is LoadingAnimation.Default -> {
//                    // default ProgressBar is visible by default in the layout
//                }
//                is LoadingAnimation.Lottie -> {
//                    setupLottieFromAssets(view, anim.assetPath)
//                }
//                is LoadingAnimation.LottieRes -> {
//                    setupLottieFromRes(view, anim.rawRes)
//                }
//                is LoadingAnimation.Drawable -> {
//                    setupDrawable(view, anim.drawableRes)
//                }
//            }
//
//            setContentView(view)
//        }
//    }
//
//    private fun setupLottieFromAssets(view: android.view.View, assetPath: String) {
//        // consumer must have Lottie in their app
//        // we resolve LottieAnimationView via reflection to avoid hard dependency
//        try {
//            val lottieView = view.findViewById<android.view.View>(R.id.signal_loading_lottie)
//            lottieView.visibility = android.view.View.VISIBLE
//            view.findViewById<android.view.View>(R.id.signal_loading_spinner).visibility =
//                android.view.View.GONE
//            val method = lottieView.javaClass.getMethod("setAnimationFromAssets", String::class.java)
//            method.invoke(lottieView, assetPath)
//            val playMethod = lottieView.javaClass.getMethod("playAnimation")
//            playMethod.invoke(lottieView)
//        } catch (e: Exception) {
//            // Lottie not available — fallback to spinner silently
//        }
//    }
//
//    private fun setupLottieFromRes(view: android.view.View, rawRes: Int) {
//        try {
//            val lottieView = view.findViewById<android.view.View>(R.id.signal_loading_lottie)
//            lottieView.visibility = android.view.View.VISIBLE
//            view.findViewById<android.view.View>(R.id.signal_loading_spinner).visibility =
//                android.view.View.GONE
//            val method = lottieView.javaClass.getMethod("setAnimation", Int::class.java)
//            method.invoke(lottieView, rawRes)
//            val playMethod = lottieView.javaClass.getMethod("playAnimation")
//            playMethod.invoke(lottieView)
//        } catch (e: Exception) {
//            // fallback silently
//        }
//    }
//
//    private fun setupDrawable(view: android.view.View, drawableRes: Int) {
//        try {
//            val imageView = view.findViewById<android.widget.ImageView>(R.id.signal_loading_lottie)
//            imageView.visibility = android.view.View.VISIBLE
//            view.findViewById<android.view.View>(R.id.signal_loading_spinner).visibility =
//                android.view.View.GONE
//            imageView.setImageResource(drawableRes)
//        } catch (e: Exception) {
//            // fallback silently
//        }
//    }
//
//    override fun show() = show(defaultConfig.message)
//
//    override fun dismiss() {
//        currentDialog?.dismiss()
//        currentDialog = null
//    }
//
//    fun updateMessage(message: String) {
//        currentDialog?.findViewById<TextView>(R.id.signal_loading_message)?.text = message
//    }
//}