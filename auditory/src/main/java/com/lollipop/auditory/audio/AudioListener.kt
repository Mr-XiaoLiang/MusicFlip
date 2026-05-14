package com.lollipop.auditory.audio

interface AudioListener {

    fun onAudioBegin()

    fun onAudioProgress(ms: Long)

    fun onPlay()

    fun onPause()

    fun onAudioEnd()

    fun onPlayerError(msg: String)

}