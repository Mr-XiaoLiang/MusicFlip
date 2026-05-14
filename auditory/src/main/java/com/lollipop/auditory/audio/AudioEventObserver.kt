package com.lollipop.auditory.audio

import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import com.lollipop.common.tools.LLog.Companion.registerLog
import com.lollipop.common.tools.task

class AudioEventObserver(
    private val progressCallback: () -> Long
) {

    private val log = registerLog()

    val playerListener: Player.Listener = PlayerListenerWrapper(this)

    var focusListener: AudioListener? = null
        private set

    var updateInterval = 100L

    private val listenerManager = ArrayList<AudioListener>()

    private var isPlaying = false

    private val progressUpdateTask = task {
        onProgressUpdate()
    }

    fun add(listener: AudioListener) {
        listenerManager.add(listener)
    }

    fun remove(listener: AudioListener) {
        listenerManager.remove(listener)
    }

    fun setFocus(listener: AudioListener?) {
        focusListener = listener
    }

    private fun invoke(block: AudioListener.() -> Unit) {
        focusListener?.block()
        for (listener in listenerManager) {
            listener.block()
        }
    }

    private fun onAudioReady() {
        invoke { onAudioBegin() }
    }

    private fun onAudioEnded() {
        invoke { onAudioEnd() }
    }

    private fun onAudioPlayingChanged(isPlaying: Boolean) {
        this.isPlaying = isPlaying
        progressUpdateTask.cancel()
        onProgressUpdate()
        if (isPlaying) {
            invoke { onPlay() }
        } else {
            invoke { onPause() }
        }
    }

    private fun onPlayerError(error: PlaybackException) {
        val msg = "Code: ${error.errorCodeName}, Message: ${error.message ?: "Unknown"}"
        log.e("onPlayerError", error)
        invoke { onPlayerError(msg) }
    }

    fun notifyProgressUpdate() {
        onProgressUpdate()
    }

    private fun onProgressUpdate() {
        val progress = progressCallback()

        invoke { onAudioProgress(progress) }

        if (isPlaying) {
            progressUpdateTask.delayOnUI(updateInterval)
        }
    }

    private class PlayerListenerWrapper(
        private val observer: AudioEventObserver
    ) : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> observer.onAudioEnded()
            }
        }

        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
            observer.onAudioReady()
        }

        override fun onPlayerError(error: PlaybackException) {
            observer.onPlayerError(error)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            observer.onAudioPlayingChanged(isPlaying)
        }

    }

}