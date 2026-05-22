package com.lollipop.mediaflow.tools

import android.app.Activity
import android.app.PictureInPictureParams
import android.util.Rational
import com.lollipop.mediaflow.data.MediaMetadata

object PIPHelper {

    private val maxRational by lazy {
        Rational(239, 100)
    }

    private val minRational by lazy {
        Rational(100, 239)
    }

    private const val MIN_RATIO = 1f / 2.39f
    private const val MAX_RATIO = 2.39f / 1f

    fun setParams(activity: Activity, metadata: MediaMetadata?) {
        val width = metadata?.width ?: 100
        val height = metadata?.height ?: 100
//        if (metadata?.needRotate == true) {
//            setParams(activity = activity, width = height, height = width)
//        } else {
//            setParams(activity = activity, width = width, height = height)
//        }
        setParams(activity = activity, width = width, height = height)
    }

    fun setParams(activity: Activity, width: Int, height: Int) {
        // 1. 防止极端比例崩溃，Android 限制比例必须在 1:2.39 到 2.39:1 之间
        val rawRatio = width.toFloat() / height.toFloat()
        val finalRatio = when {
            rawRatio > MAX_RATIO -> maxRational
            rawRatio < MIN_RATIO -> minRational
            else -> Rational(width, height)
        }

        // 2. 构建新的参数
        val updatedParams = PictureInPictureParams.Builder()
            .setAspectRatio(finalRatio)
            .setAutoEnterEnabled(true)
            .build()

        // 3. 动态注入（无论是在前台还是已经处于画中画，都会实时生效）
        activity.setPictureInPictureParams(updatedParams)
    }

    fun isInPictureInPictureMode(activity: Activity): Boolean {
        return activity.isInPictureInPictureMode
    }

}