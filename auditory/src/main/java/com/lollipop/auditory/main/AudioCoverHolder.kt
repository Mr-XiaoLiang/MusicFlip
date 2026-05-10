package com.lollipop.auditory.main

import android.annotation.SuppressLint
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.databinding.ItemPlaySongBinding
import com.lollipop.auditory.ui.view.CoverViewPagerAdapter
import com.lollipop.common.ui.page.GuidelineInsetsHelper

class AudioCoverHolder(
    private val binding: ItemPlaySongBinding
) : CoverViewPagerAdapter.ViewHolder(binding.root) {

    val guidelineInsetsHelper = GuidelineInsetsHelper().also {
        it.bindGuidelineInsets(
            context = binding.root.context,
            leftGuideline = binding.startGuideLine,
            topGuideline = binding.topGuideLine,
            rightGuideline = binding.endGuideLine,
            bottomGuideline = binding.bottomGuideLine
        )
    }

    @SuppressLint("SetTextI18n")
    fun bind(audioInfo: AudioInfo, allCount: Int, position: Int) {
        binding.titleTextView.text = audioInfo.title
        binding.pageNumberView.text = "${position + 1}/$allCount"
    }

    fun testBind(title: String, pageNumber: String) {
        binding.titleTextView.text = title
        binding.pageNumberView.text = pageNumber
    }

}