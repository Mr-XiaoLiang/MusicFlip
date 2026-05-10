package com.lollipop.auditory.main

import android.annotation.SuppressLint
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.databinding.ItemPlayerMiniBinding
import com.lollipop.auditory.main.basic.BasicCoverHolder
import com.lollipop.common.ui.page.GuidelineInsetsHelper

class MiniCoverHolder(
    private val binding: ItemPlayerMiniBinding
) : BasicCoverHolder(binding.root) {

    override fun updateEdgeSize(edgeSize: GuidelineInsetsHelper.EdgeSize) {
        GuidelineInsetsHelper.update(
            edgeSize = edgeSize,
            leftGuideline = binding.startGuideLine,
            topGuideline = binding.topGuideLine,
            rightGuideline = binding.endGuideLine,
            bottomGuideline = binding.bottomGuideLine
        )
    }

    @SuppressLint("SetTextI18n")
    override fun bind(audioInfo: AudioInfo, allCount: Int, position: Int) {
        binding.titleView.text = audioInfo.title
    }

    fun testBind(title: String, pageNumber: String) {
        binding.titleView.text = title
    }

}