package com.lollipop.common.ui.page

import android.graphics.Rect
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.lollipop.common.tools.LLog.Companion.registerLog

abstract class BasicInsetsActivity : AppCompatActivity(), InsetsFragment.Provider {

    protected val log by lazy {
        registerLog()
    }

    protected val insetsProviderHelper = InsetsFragment.ProviderHelper()

    private val guidelineInsetsGroup = GuidelineInsetsGroup()

    protected open val checkSystemBarsInsets = true
    protected open val checkDisplayCutoutInsets = true
    protected open val checkSystemGesturesInsets = false

    protected var insetsCache = Insets.NONE
        private set

    protected fun setAppearanceLightStatusBars(isLight: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView).also {
            it.isAppearanceLightStatusBars = isLight
        }
    }

    fun registerGuidelineInsetsListener(helper: GuidelineInsetsHelper) {
        guidelineInsetsGroup.register(helper)
    }

    fun unregisterGuidelineInsetsListener(helper: GuidelineInsetsHelper) {
        guidelineInsetsGroup.unregister(helper)
    }

    private fun findInsets(insets: WindowInsetsCompat): Insets {
        val systemBars = if (checkSystemBarsInsets) {
            insets.getInsets(WindowInsetsCompat.Type.systemBars())
        } else {
            Insets.NONE
        }
        val displayCutout = if (checkDisplayCutoutInsets) {
            insets.getInsets(WindowInsetsCompat.Type.displayCutout())
        } else {
            Insets.NONE
        }
        val systemGestures = if (checkSystemGesturesInsets) {
            insets.getInsets(WindowInsetsCompat.Type.systemGestures())
        } else {
            Insets.NONE
        }

        return Insets.of(
            max(systemBars.left, displayCutout.left, systemGestures.left),
            max(systemBars.top, displayCutout.top, systemGestures.top),
            max(systemBars.right, displayCutout.right, systemGestures.right),
            max(systemBars.bottom, displayCutout.bottom, systemGestures.bottom)
        )
    }

    private fun max(vararg values: Int): Int {
        var max = values[0]
        for (i in 1 until values.size) {
            if (values[i] > max) {
                max = values[i]
            }
        }
        return max
    }

    protected fun initInsetsListener(rootView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            insetsCache = findInsets(insets)
            insetsProviderHelper.updateInsets(
                insetsCache.left,
                insetsCache.top,
                insetsCache.right,
                insetsCache.bottom
            )
            onWindowInsetsChanged(
                insetsCache.left,
                insetsCache.top,
                insetsCache.right,
                insetsCache.bottom
            )
            updateGuidelineInsets(
                insetsCache.left,
                insetsCache.top,
                insetsCache.right,
                insetsCache.bottom
            )
            insets
        }
    }

    protected abstract fun onWindowInsetsChanged(
        left: Int, top: Int, right: Int, bottom: Int
    )

    override fun getInsets(): Rect {
        return insetsProviderHelper.getInsets()
    }

    override fun registerInsetsListener(listener: InsetsFragment.InsetsListener) {
        insetsProviderHelper.registerInsetsListener(listener)
    }

    override fun unregisterInsetsListener(listener: InsetsFragment.InsetsListener) {
        insetsProviderHelper.unregisterInsetsListener(listener)
    }

    private fun updateGuidelineInsets(
        left: Int, top: Int, right: Int, bottom: Int
    ) {
        log.i("updateGuidelineInsets: $left, $top, $right, $bottom")
        guidelineInsetsGroup.updateGuidelineInsets(left, top, right, bottom)
    }

    protected class GuidelineInsetsGroup {

        private val insetsListener = mutableListOf<GuidelineInsetsHelper>()

        fun register(helper: GuidelineInsetsHelper) {
            insetsListener.add(helper)
        }

        fun unregister(helper: GuidelineInsetsHelper) {
            insetsListener.remove(helper)
        }

        fun updateGuidelineInsets(
            left: Int, top: Int, right: Int, bottom: Int
        ) {
            insetsListener.forEach { it.updateGuidelineInsets(left, top, right, bottom) }
        }

    }

}