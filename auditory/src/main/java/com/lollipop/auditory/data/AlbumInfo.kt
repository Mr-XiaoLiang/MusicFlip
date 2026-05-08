package com.lollipop.auditory.data

import android.content.ContentUris
import android.provider.MediaStore

class AlbumInfo(
    val id: Long,
    val title: String,
    val artist: String,
    val year: Int,
    val dateAdded: Long,
    val songs: List<AudioInfo>,
) {
    companion object {
        fun build(id: Long, songs: List<AudioInfo>): AlbumInfo {
            val title = if (songs.isEmpty()) {
                ""
            } else {
                songs.first().album
            }
            val artist = if (songs.isEmpty()) {
                ""
            } else {
                songs.first().albumArtist
            }
            val year = if (songs.isEmpty()) {
                0
            } else {
                songs.maxBy { it.year }.year
            }
            val dateAdded = if (songs.isEmpty()) {
                0L
            } else {
                songs.maxBy { it.dateAdded }.dateAdded
            }
            return AlbumInfo(
                id = id,
                title = title,
                artist = artist,
                year = year,
                dateAdded = dateAdded,
                songs = songs.sortedBy { it.trackNumber },
            )
        }
    }

    /**
     * 专辑封面URI
     */
    val cover = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id)

    /**
     * 专辑中歌曲数量
     */
    val songCount = songs.size

}