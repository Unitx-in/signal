package com.unitx.signal_core.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.unitx.signal_core.R
import com.unitx.signal_core.contract.model.LoadingAnimationAttr
import com.unitx.signal_core.helper.LoadingDotsAnimator

internal class LoadingDotsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var animationConfig: LoadingAnimationAttr =
        LoadingAnimationAttr()
        set(value) {
            field = value

            if (::animator.isInitialized) {
                animator.stop()
                createAnimator()

                if (isAttachedToWindow) {
                    animator.start()
                }
            }
        }


    var activeColor: Int = ContextCompat.getColor(context, R.color.signalBlack)
    var inactiveColor : Int = ContextCompat.getColor(context, R.color.signalGray)

    private lateinit var animator: LoadingDotsAnimator

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.signal_loading_dots, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        createAnimator()
    }

    fun applyColors(activeColor: Int, inactiveColor: Int) {
        if (::animator.isInitialized) {
            animator.updateColors(activeColor, inactiveColor)
        }
    }

    private fun createAnimator() {

        animator = LoadingDotsAnimator(
            dots = listOf(
                findViewById(R.id.dot1),
                findViewById(R.id.dot2),
                findViewById(R.id.dot3),
                findViewById(R.id.dot4)
            ),
            activeColor = this.activeColor,
            inactiveColor = this.inactiveColor,
            config = animationConfig
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        animator.stop()
        super.onDetachedFromWindow()
    }
}