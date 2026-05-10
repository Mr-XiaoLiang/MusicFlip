package com.lollipop.auditory.main.basic

import android.view.View
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.ui.view.CoverViewPagerAdapter

abstract class BasicCoverHolder(
    pageView: View
) : CoverViewPagerAdapter.ViewHolder(pageView) {

    abstract fun bind(audioInfo: AudioInfo, allCount: Int, position: Int)

}