package com.lollipop.auditory.main

import com.lollipop.common.ui.page.GuidelineInsetsHelper
import com.lollipop.common.ui.page.PageOrientation

abstract class BasicSheetPanel {

    val guidelineInsetsHelper = GuidelineInsetsHelper().also {
        it.onChanged(::onGuidelineInsetsChanged)
    }

    open fun onCreate() {
    }

    protected open fun onGuidelineInsetsChanged(
        left: Int, top: Int, right: Int, bottom: Int
    ) {
    }

    open fun onSlide(offset: Float) {

    }

    open fun onExpand() {

    }

    open fun onCollapse() {

    }

    open fun onResume() {

    }

    open fun onPause() {

    }

    open fun onOrientationChanged(orientation: PageOrientation) {}

    protected class SlideHelper(
        val minOffset: Float,
        val maxOffset: Float,
    ) {

        fun onSlide(offset: Float): Float {
            if (offset < minOffset) {
                return 0F
            }
            if (offset > maxOffset) {
                return 1F
            }
            return (offset - minOffset) / (maxOffset - minOffset)
        }

    }

}