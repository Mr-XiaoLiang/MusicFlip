package com.lollipop.common.ui.page

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import androidx.core.view.updateLayoutParams
import com.lollipop.common.tools.LLog.Companion.registerLog

class GuidelineInsetsHelper {

    private val log by lazy {
        registerLog()
    }

    private var leftGuideline: View? = null
    private var topGuideline: View? = null
    private var rightGuideline: View? = null
    private var bottomGuideline: View? = null
    var minEdge: Int = 0
        private set

    private var callback: OnGuidelineInsetsChangedCallback? = null

    private var filter: OnGuidelineInsetsFilter? = null

    fun onFilter(filter: OnGuidelineInsetsFilter?) {
        this.filter = filter
    }

    fun onChanged(callback: OnGuidelineInsetsChangedCallback?) {
        this.callback = callback
    }

    fun bindGuidelineInsets(
        context: Context,
        leftGuideline: View?,
        topGuideline: View?,
        rightGuideline: View?,
        bottomGuideline: View?,
        minEdgeDp: Float = 16F
    ) {
        minEdge = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            minEdgeDp,
            context.resources.displayMetrics
        ).toInt()
        this.leftGuideline = leftGuideline
        this.topGuideline = topGuideline
        this.rightGuideline = rightGuideline
        this.bottomGuideline = bottomGuideline
    }

    fun updateGuidelineInsets(
        left: Int, top: Int, right: Int, bottom: Int
    ) {
        log.i("updateGuidelineInsets: $left, $top, $right, $bottom")
        val guidelineInsets = filterGuidelineInsets(
            Insets.of(
                maxOf(left, minEdge),
                maxOf(top, minEdge),
                maxOf(right, minEdge),
                maxOf(bottom, minEdge)
            )
        )
        onGuidelineInsetsChanged(
            guidelineInsets.left,
            guidelineInsets.top,
            guidelineInsets.right,
            guidelineInsets.bottom
        )
        try {
            leftGuideline?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                guideBegin = guidelineInsets.left
            }
        } catch (e: Throwable) {
            log.e("updateGuidelineInsets: left", e)
        }
        try {
            topGuideline?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                guideBegin = guidelineInsets.top
            }
        } catch (e: Throwable) {
            log.e("updateGuidelineInsets: top", e)
        }
        try {
            rightGuideline?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                guideEnd = guidelineInsets.right
            }
        } catch (e: Throwable) {
            log.e("updateGuidelineInsets: right", e)
        }
        try {
            bottomGuideline?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                guideEnd = guidelineInsets.bottom
            }
        } catch (e: Throwable) {
            log.e("updateGuidelineInsets: bottom", e)
        }
    }

    private fun onGuidelineInsetsChanged(
        left: Int, top: Int, right: Int, bottom: Int
    ) {
        callback?.onGuidelineInsetsChanged(left, top, right, bottom)
    }

    private fun filterGuidelineInsets(insets: Insets): Insets {
        return filter?.filterGuidelineInsets(insets) ?: insets
    }

    fun interface OnGuidelineInsetsChangedCallback {
        fun onGuidelineInsetsChanged(
            left: Int, top: Int, right: Int, bottom: Int
        )
    }

    fun interface OnGuidelineInsetsFilter {
        fun filterGuidelineInsets(insets: Insets): Insets
    }

}