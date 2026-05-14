package com.lollipop.auditory.audio

import android.app.Activity
import android.content.Intent

object AudioServiceHelper {

    fun startForegroundService(activity: Activity, intent: Intent) {

    }

    // 针对你的本地文件场景，你可以这样构建带有元数据的 MediaItem
    //val mediaItem = MediaItem.Builder()
    //    .setMediaId("unique_song_id")
    //    .setUri(localFileUri) // 本地音频文件的 Uri
    //    .setMediaMetadata(
    //        MediaMetadata.Builder()
    //            .setTitle("歌名：七里香")
    //            .setArtist("歌手：周杰伦")
    //            .setAlbumTitle("专辑：七里香")
    //            // 如果你有本地解析出来的封面图片 Bitmap，可以直接转成字节数组传给系统
    //            // .setArtworkData(artworkByteArray, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
    //            .build()
    //    )
    //    .build()
    //
    //player.setMediaItem(mediaItem)

}