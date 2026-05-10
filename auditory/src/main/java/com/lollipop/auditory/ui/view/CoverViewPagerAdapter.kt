package com.lollipop.auditory.ui.view

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import java.util.LinkedList
import androidx.core.util.size
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.common.ui.page.GuidelineInsetsHelper

abstract class CoverViewPagerAdapter<VH : CoverViewPagerAdapter.ViewHolder> : PagerAdapter() {

    private val holderCache = LinkedList<VH>()
    private val activeHolders = SparseArray<VH>()

    override fun isViewFromObject(view: View, holder: Any): Boolean {
        if (holder is ViewHolder) {
            return view === holder.pageView
        }
        return false
    }

    private fun getHolder(container: ViewGroup): VH {
        return if (holderCache.isEmpty()) {
            createViewHolder(container)
        } else {
            holderCache.removeFirst()
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val holder = getHolder(container)
        activeHolders.set(position, holder)
        bindViewHolder(holder, position)
        container.addView(
            holder.pageView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return holder
    }

    override fun destroyItem(container: ViewGroup, position: Int, holder: Any) {
        if (holder is ViewHolder) {
            activeHolders.remove(position)
            container.removeView(holder.pageView)
            @Suppress("UNCHECKED_CAST")
            holderCache.add(holder as VH)
        }
    }

    fun findHolder(position: Int): VH? {
        return activeHolders[position]
    }

    fun forEachActive(action: (VH) -> Unit) {
        val size = activeHolders.size
        for (i in 0 until size) {
            action(activeHolders.valueAt(i))
        }
    }

    fun notifyItemChanged(position: Int) {
        activeHolders[position]?.let { holder ->
            bindViewHolder(holder, position)
        }
    }

    fun updateEdgeSize(edgeSize: GuidelineInsetsHelper.EdgeSize) {
        forEachActive {
            it.updateEdgeSize(edgeSize)
        }
    }

    abstract fun createViewHolder(container: ViewGroup): VH

    abstract fun bindViewHolder(holder: VH, position: Int)

    abstract class ViewHolder(val pageView: View) {

        abstract fun updateEdgeSize(edgeSize: GuidelineInsetsHelper.EdgeSize)

    }

}