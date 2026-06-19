package com.unitx.signal_core.view.loading

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.unitx.signal_core.contract.config.LoadingConfig
import com.unitx.signal_core.databinding.SignalLoadingSimpleBinding
import com.unitx.signal_core.helper.DimOverlay
import com.unitx.signal_core.helper.rootViewGroup
import com.unitx.signal_core.provider.ActivityProvider
import com.unitx.signal_core.theme.SignalThemeResolver

internal class SimpleLoadingViewManager(
    private val activityProvider: ActivityProvider,
    private val themeResolver: SignalThemeResolver
) : ILoadingViewManager {

    private var binding: SignalLoadingSimpleBinding? = null
    private val dim = DimOverlay()

    override val container: View?
        get() = binding?.loadingDots

    override val isShowing: Boolean
        get() = binding?.loadingDots?.visibility == View.VISIBLE

    override fun attach(config: LoadingConfig, onDismiss: () -> Unit): Boolean {
        val activity = activityProvider.current() ?: return false
        val rootView = activity.rootViewGroup() ?: return false

        dim.attach(activity, rootView, config.cancelable, onDismiss)
        inflate(activity, rootView)
        applyTheme(activity)
        binding?.root?.contentDescription = config.accessibilityText ?: "Loading"
        binding?.loadingDots?.animationConfig = config.animationAttr
        return true
    }

    private fun inflate(context: Context, rootView: ViewGroup) {
        if (binding != null) return
        binding = SignalLoadingSimpleBinding.inflate(LayoutInflater.from(context), rootView, false)
        rootView.addView(
            binding!!.root,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER }
        )
    }

    private fun applyTheme(context: Context) {
        val b = binding ?: return
        val scheme = themeResolver.resolve(context)
        val active = scheme.loadingSimpleAnimationActiveColor ?: return
        val inactive = scheme.loadingSimpleAnimationInactiveColor ?: return
        b.loadingDots.applyColors(active, inactive)
    }

    override fun release(onReleased: () -> Unit) {
        val rootView = activityProvider.current()?.rootViewGroup()
        dim.release(rootView) {
            binding?.root?.let { rootView?.removeView(it) }
            binding = null
            onReleased()
        }
    }

    // no-op — simple loading has no progress concept
    override fun updateProgress(config: LoadingConfig) {}
}