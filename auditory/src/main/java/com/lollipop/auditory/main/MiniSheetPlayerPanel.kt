package com.lollipop.auditory.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.lollipop.auditory.databinding.FragmentPlayerMiniBinding
import com.lollipop.auditory.databinding.ItemPlayerMiniBinding
import com.lollipop.auditory.ui.view.CoverViewPagerAdapter
import com.lollipop.common.ui.view.SizeObserverFrameLayout

class MiniSheetPlayerPanel(
    val binding: FragmentPlayerMiniBinding
) : BasicSheetPanel() {

    private val slideHelper = SlideHelper(0.2F, 0.8F)

    fun onSizeChanged(callback: SizeObserverFrameLayout.OnSizeChangedListener?) {
        binding.root.onSizeChanged(callback)
    }

    override fun onCreate() {
        guidelineInsetsHelper.bindGuidelineInsets(
            context = binding.root.context,
            leftGuideline = binding.startGuideLine,
            topGuideline = null,
            rightGuideline = binding.endGuideLine,
            bottomGuideline = binding.bottomGuideLine
        )

        binding.coverViewPager.adapter = object : CoverViewPagerAdapter<CoverHolder>() {

            private val layoutInflater = LayoutInflater.from(binding.coverViewPager.context)

            override fun getCount(): Int {
                return 20
            }

            override fun createViewHolder(container: ViewGroup): CoverHolder {
                return CoverHolder(ItemPlayerMiniBinding.inflate(layoutInflater))
            }

            override fun bindViewHolder(
                holder: CoverHolder,
                position: Int
            ) {
                holder.binding.titleView.text = "Holder $position"
            }

        }
    }

    class CoverHolder(val binding: ItemPlayerMiniBinding) :
        CoverViewPagerAdapter.ViewHolder(binding.root) {

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

}