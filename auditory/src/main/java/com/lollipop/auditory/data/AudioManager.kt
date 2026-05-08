package com.lollipop.auditory.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore

object AudioManager {

    enum class AudioColumn(val key: String) {
        ID(key = MediaStore.Audio.Media._ID),
        TITLE(key = MediaStore.Audio.Media.TITLE),
        ARTIST(key = MediaStore.Audio.Media.ARTIST),
        ARTIST_ID(key = MediaStore.Audio.Media.ARTIST_ID),
        ALBUM(key = MediaStore.Audio.Media.ALBUM),
        ALBUM_ARTIST(key = MediaStore.Audio.Media.ALBUM_ARTIST), // Android 10+ 支持
        COMPOSER(key = MediaStore.Audio.Media.COMPOSER),     // 作曲家
        DURATION(key = MediaStore.Audio.Media.DURATION),
        SIZE(key = MediaStore.Audio.Media.SIZE),
        DATA(key = MediaStore.Audio.Media.DATA),           // 文件路径
        BUCKET_DISPLAY_NAME(key = MediaStore.Audio.Media.BUCKET_DISPLAY_NAME),
        DISPLAY_NAME(key = MediaStore.Audio.Media.DISPLAY_NAME),
        ALBUM_ID(key = MediaStore.Audio.Media.ALBUM_ID),
        DATE_ADDED(key = MediaStore.Audio.Media.DATE_ADDED),
        TRACK(key = MediaStore.Audio.Media.TRACK),
        YEAR(key = MediaStore.Audio.Media.YEAR);

        fun getIndex(cursor: Cursor): Int {
            return cursor.getColumnIndex(key)
        }
    }

    fun loadSync(context: Context, limit: Int = 200, offset: Int = 0): List<AudioInfo> {
        val songList = mutableListOf<AudioInfo>()
        val contentResolver = context.contentResolver

        // 2. 配置我们想要“一口气带走”的所有字段
        val projection = AudioColumn.entries.map { it.key }.toTypedArray()

        // 3. 配置分页和排序（Android 11+ 推荐用法）
        val queryArgs = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
            putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
            // 按照ID的顺序
            putStringArray(
                ContentResolver.QUERY_ARG_SORT_COLUMNS,
                arrayOf(MediaStore.Audio.Media._ID)
            )
            putInt(
                ContentResolver.QUERY_ARG_SORT_DIRECTION,
                ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
            )
        }

        // 4. 执行查询
        val collectionUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        contentResolver.query(collectionUri, projection, queryArgs, null)?.use { cursor ->
            // 获取列索引（建议先缓存索引，避免在循环中重复寻找）
            val audioCursor = AudioCursor(cursor)

            while (cursor.moveToNext()) {
                songList.add(
                    AudioInfo(
                        id = audioCursor.getId(),
                        title = audioCursor.getTitle(),
                        artist = audioCursor.getArtist(),
                        artistId = audioCursor.getArtistId(),
                        album = audioCursor.getAlbum(),
                        albumArtist = audioCursor.getAlbumArtist(),
                        composer = audioCursor.getComposer(),
                        duration = audioCursor.getDuration(),
                        size = audioCursor.getSize(),
                        path = audioCursor.getPath(),
                        folderName = audioCursor.getFolderName(),
                        displayName = audioCursor.getDisplayName(),
                        albumId = audioCursor.getAlbumId(),
                        dateAdded = audioCursor.getDateAdded(),
                        trackNumber = audioCursor.getTrack(),
                        year = audioCursor.getYear()
                    )
                )
            }
        }
        return songList
    }

    private class AudioCursor(val cursor: Cursor) {

        val idCol = AudioColumn.ID.getIndex(cursor)
        val titleCol = AudioColumn.TITLE.getIndex(cursor)
        val artistCol = AudioColumn.ARTIST.getIndex(cursor)
        val artistIdCol = AudioColumn.ARTIST_ID.getIndex(cursor)
        val albumCol = AudioColumn.ALBUM.getIndex(cursor)
        val albumArtistCol = AudioColumn.ALBUM_ARTIST.getIndex(cursor)
        val composerCol = AudioColumn.COMPOSER.getIndex(cursor)
        val durationCol = AudioColumn.DURATION.getIndex(cursor)
        val sizeCol = AudioColumn.SIZE.getIndex(cursor)
        val dataCol = AudioColumn.DATA.getIndex(cursor)
        val bucketDisplayNameCol = AudioColumn.BUCKET_DISPLAY_NAME.getIndex(cursor)
        val displayNameCol = AudioColumn.DISPLAY_NAME.getIndex(cursor)
        val albumIdCol = AudioColumn.ALBUM_ID.getIndex(cursor)
        val dateAddedCol = AudioColumn.DATE_ADDED.getIndex(cursor)
        val trackCol = AudioColumn.TRACK.getIndex(cursor)
        val yearCol = AudioColumn.YEAR.getIndex(cursor)

        fun getId(): Long {
            return getLong(idCol, 0L)
        }

        fun getTitle(): String {
            return getString(titleCol, "")
        }

        fun getArtist(): String {
            return getString(artistCol, "")
        }

        fun getArtistId(): Long {
            return getLong(artistIdCol, 0)
        }

        fun getAlbum(): String {
            return getString(albumCol, "")
        }

        fun getAlbumArtist(): String {
            return getString(albumArtistCol, "")
        }

        fun getComposer(): String {
            return getString(composerCol, "")
        }

        fun getDuration(): Long {
            return getLong(durationCol, 0L)
        }

        fun getSize(): Long {
            return getLong(sizeCol, 0L)
        }

        fun getPath(): String {
            return getString(dataCol, "")
        }

        fun getFolderName(): String {
            return getString(bucketDisplayNameCol, "")
        }

        fun getDisplayName(): String {
            return getString(displayNameCol, "")
        }

        fun getAlbumId(): Long {
            return getLong(albumIdCol, 0L)
        }

        fun getDateAdded(): Long {
            return getLong(dateAddedCol, 0L) * 1000L
        }

        fun getTrack(): Int {
            return getInt(trackCol, 0)
        }

        fun getYear(): Int {
            return getInt(yearCol, 0)
        }

        private fun getString(index: Int, def: String): String {
            if (index < 0) {
                return def
            }
            return cursor.getString(index)
        }

        private fun getLong(index: Int, def: Long): Long {
            if (index < 0) {
                return def
            }
            return cursor.getLong(index)
        }

        private fun getInt(index: Int, def: Int): Int {
            if (index < 0) {
                return def
            }
            return cursor.getInt(index)
        }

    }

}