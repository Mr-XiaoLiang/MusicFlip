package com.lollipop.auditory.state

import android.content.Context
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.data.AudioManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

object AudioRepository {

    private val audioListFlow by lazy {
        MutableStateFlow<List<AudioInfo>>(emptyList())
    }

    private val stateFlow by lazy {
        MutableStateFlow<State>(State.IDLE)
    }

    val audioList by lazy {
        audioListFlow.asStateFlow()
    }

    val state by lazy {
        stateFlow.asStateFlow()
    }

    suspend fun refreshAudioList(context: Context) {
        if (state.value == State.LOADING) {
            return
        }
        stateFlow.update {
            State.LOADING
        }
        withContext(Dispatchers.IO) {
            var index = 0
            while (true) {
                val pageSize = 200
                val offset = pageSize * index
                val pageList = AudioManager.loadSync(
                    context = context,
                    limit = pageSize,
                    offset = offset
                )
                if (pageList.isEmpty()) {
                    break
                }
                audioListFlow.update {
                    if (index == 0) {
                        pageList
                    } else {
                        it + pageList
                    }
                }
                index++
                // 可以在每一页之间稍微 yield 一下，让出 CPU 给 UI 线程处理渲染
                kotlinx.coroutines.yield()
            }
            stateFlow.update {
                State.IDLE
            }
        }
    }

    enum class State {
        LOADING,
        IDLE,
    }

}