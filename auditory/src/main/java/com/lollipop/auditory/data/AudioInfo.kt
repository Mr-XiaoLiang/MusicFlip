package com.lollipop.auditory.data

import android.content.ContentUris
import android.provider.MediaStore

class AudioInfo(
    /**
     * 歌曲ID，用于唯一标识一首歌曲
     */
    val id: Long,
    /**
     * 歌曲标题
     */
    val title: String,
    /**
     * 歌手名
     */
    val artist: String,
    /**
     * 歌手ID
     */
    val artistId: Long,
    /**
     * 专辑名
     */
    val album: String,
    /**
     * 专辑艺术家名
     */
    val albumArtist: String,
    /**
     * 作曲家名
     */
    val composer: String,
    /**
     * 歌曲时长（毫秒）
     */
    val duration: Long,
    /**
     * 歌曲大小（字节）
     */
    val size: Long,
    /**
     * 歌曲文件路径
     */
    val path: String,
    /**
     * 歌曲所属文件夹名
     */
    val folderName: String,
    /**
     * 专辑ID
     */
    val albumId: Long,
    /**
     * 显示名称
     */
    val displayName: String,
    /**
     * 添加时间
     */
    val dateAdded: Long,
    /**
     * 专辑中的歌曲序号
     */
    val trackNumber: Int,
    /**
     * 年份
     */
    val year: Int
) {

    val titleHash = title.hashCode()

    /**
     * 歌曲的URI
     */
    val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

    /**
     * 专辑封面URI
     */
    val albumUri = ContentUris.withAppendedId(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, albumId)

    /**
     * 歌曲名称，如果为空则使用显示名称
     */
    val audioName: String by lazy {
        title.ifBlank { displayName }
    }

    companion object {
        val EMPTY = AudioInfo(
            id = 0,
            title = "",
            artist = "",
            artistId = 0,
            album = "",
            albumArtist = "",
            composer = "",
            duration = 0,
            size = 0,
            path = "",
            folderName = "",
            displayName = "",
            albumId = 0,
            dateAdded = 0,
            trackNumber = 0,
            year = 0,
        )
    }

}