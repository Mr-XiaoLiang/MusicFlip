package com.lollipop.auditory.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.lollipop.auditory.databinding.FragmentPlayerMiniBinding
import com.lollipop.auditory.databinding.ItemPlayerMiniBinding
import com.lollipop.auditory.main.basic.BasicSheetPanel
import com.lollipop.auditory.ui.view.CoverViewPagerAdapter
import com.lollipop.common.ui.page.GuidelineInsetsHelper
import com.lollipop.common.ui.view.SizeObserverFrameLayout

class MiniSheetPlayerPanel(
    val binding: FragmentPlayerMiniBinding
) : BasicSheetPanel() {

    private val slideHelper = SlideHelper(0.2F, 0.8F)

    private val pagerAdapter = CoverPagerAdapter(binding.root.context) {
        guidelineInsetsHelper.current
    }

    fun onSizeChanged(callback: SizeObserverFrameLayout.OnSizeChangedListener?) {
        binding.root.onSizeChanged(callback)
    }

    override fun onCreate() {
        guidelineInsetsHelper.setMinEdge(
            context = binding.root.context,
            leftDp = 16F,
            topDp = 12F,
            rightDp = 16F,
            bottomDp = 24F
        )
        guidelineInsetsHelper.onFilter { src ->
            GuidelineInsetsHelper.SimpleEdgeSize(
                left = src.left,
                top = guidelineInsetsHelper.minEdge.top,
                right = src.right,
                bottom = src.bottom
            )
        }
        guidelineInsetsHelper.bindGuidelineInsets(
            leftGuideline = binding.startGuideLine,
            topGuideline = binding.topGuideLine,
            rightGuideline = binding.endGuideLine,
            bottomGuideline = binding.bottomGuideLine
        )
        binding.coverViewPager.adapter = pagerAdapter
    }

    override fun onGuidelineInsetsChanged(edgeSize: GuidelineInsetsHelper.EdgeSize) {
        super.onGuidelineInsetsChanged(edgeSize)
        pagerAdapter.updateEdgeSize(edgeSize)
    }

    override fun onExpand() {
        super.onExpand()
        binding.root.isInvisible = true
    }

    override fun onSlide(offset: Float) {
        if (!binding.root.isVisible) {
            binding.root.isVisible = true
        }
        binding.root.alpha = (1 - slideHelper.onSlide(offset))
    }

    private class CoverPagerAdapter(
        context: Context,
        val edgeSizeProvider: () -> GuidelineInsetsHelper.EdgeSize
    ) : CoverViewPagerAdapter<MiniCoverHolder>() {
        private val layoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return 20
        }

        override fun createViewHolder(container: ViewGroup): MiniCoverHolder {
            return MiniCoverHolder(ItemPlayerMiniBinding.inflate(layoutInflater))
        }

        override fun bindViewHolder(
            holder: MiniCoverHolder,
            position: Int
        ) {
            holder.updateEdgeSize(edgeSizeProvider())
            holder.testBind("Holder $position", "${position + 1}/${getCount()}")
        }

    }

}