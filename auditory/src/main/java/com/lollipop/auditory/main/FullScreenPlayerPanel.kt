package com.lollipop.auditory.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager
import com.lollipop.auditory.R
import com.lollipop.auditory.databinding.FragmentPlayerFullBinding
import com.lollipop.auditory.databinding.ItemPlaySongBinding
import com.lollipop.auditory.ui.view.CoverViewPagerAdapter
import com.lollipop.common.ui.page.PageOrientation

class FullScreenPlayerPanel(
    val binding: FragmentPlayerFullBinding
) : BasicSheetPanel() {

    private val coverViewPagerAdapter = CoverPagerAdapter(binding.root.context)

    private val landscapeConstraintSet by lazy {
        ConstraintSet().also {
            it.clone(binding.root.context, R.layout.fragment_player_full_landscape)
        }
    }

    private val portraitConstraintSet by lazy {
        ConstraintSet().also {
            it.clone(binding.root.context, R.layout.fragment_player_full)
        }
    }

    override fun onCreate() {
        guidelineInsetsHelper.bindGuidelineInsets(
            context = binding.root.context,
            leftGuideline = binding.startGuideLine,
            topGuideline = binding.topGuideLine,
            rightGuideline = binding.endGuideLine,
            bottomGuideline = binding.bottomGuideLine
        )
        binding.coverViewPager.adapter = coverViewPagerAdapter
    }

    override fun onGuidelineInsetsChanged(
        left: Int, top: Int, right: Int, bottom: Int
    ) {
        coverViewPagerAdapter.forEachActive {
            it.guidelineInsetsHelper.updateGuidelineInsets(left, top, right, bottom)
        }
    }

    override fun onOrientationChanged(orientation: PageOrientation) {
        val constraintLayout = binding.root
        val constraintSet = if (orientation == PageOrientation.LANDSCAPE) {
            // 加载横屏的约束定义
            landscapeConstraintSet
        } else {
            portraitConstraintSet
        }
        // 关键：开启过渡动画，让播放器从“上面”丝滑地移动到“左边”
        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet.applyTo(constraintLayout)
    }

    private class CoverPagerAdapter(
        context: Context
    ) : CoverViewPagerAdapter<AudioCoverHolder>() {
        private val layoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return 20
        }

        override fun createViewHolder(container: ViewGroup): AudioCoverHolder {
            return AudioCoverHolder(ItemPlaySongBinding.inflate(layoutInflater))
        }

        override fun bindViewHolder(
            holder: AudioCoverHolder,
            position: Int
        ) {
            holder.testBind("Holder $position", "${position + 1}/${getCount()}")
        }

    }

}