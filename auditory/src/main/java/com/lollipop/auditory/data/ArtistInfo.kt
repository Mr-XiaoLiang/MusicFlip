package com.lollipop.auditory.data

class ArtistInfo(
    val id: Long,
    val name: String,
    val songs: List<AudioInfo>,
) {

    companion object {
        fun build(id: Long, songs: List<AudioInfo>): ArtistInfo {
            val name = songs.first().artist
            return ArtistInfo(
                id = id,
                name = name,
                songs = songs,
            )
        }
    }

}