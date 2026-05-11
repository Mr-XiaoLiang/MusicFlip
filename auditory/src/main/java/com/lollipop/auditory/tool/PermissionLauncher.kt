package com.lollipop.auditory.tool

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.core.content.ContextCompat
import com.lollipop.auditory.R

val LocalPermissionLauncher = staticCompositionLocalOf {
    PermissionLauncher.Delegate(null)
}

class PermissionLauncher(
    private val activity: AppCompatActivity,
    private val onResultCallback: (Boolean) -> Unit
) {

    private val requiredPermission: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO // Android 13+
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE // Android 12 及以下
        }
    }

    private val explanationRes: Int by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            R.string.info_permission_read_media_audio_explanation
        } else {
            R.string.info_permission_read_external_storage_explanation
        }
    }

    private val requestPermissionLauncher by lazy {
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            onResult(isGranted)
        }
    }

    private val isGrantedState = mutableStateOf(false)

    val isGranted: Boolean
        get() {
            return state.value
        }

    val state: State<Boolean>
        get() {
            return isGrantedState
        }

    val delegate by lazy {
        Delegate(this)
    }

    val explanation by lazy {
        activity.getString(explanationRes)
    }

    fun onCreate() {
        // 调用来初始化并且注册
        requestPermissionLauncher
    }

    fun onResume() {
        // 检查权限并更新状态
        onResult(checkMusicPermission())
    }

    fun request() {
        requestPermissionLauncher.launch(requiredPermission)
    }

    fun checkMusicPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            requiredPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun onResult(isGranted: Boolean) {
        isGrantedState.value = isGranted
        onResultCallback(isGranted)
    }

    class Delegate(
        val launcher: PermissionLauncher?
    ) {

        val isGranted: State<Boolean> by lazy {
            launcher?.state ?: mutableStateOf(false)
        }

        val explanation: String by lazy {
            launcher?.explanation ?: ""
        }

        fun launch() {
            launcher?.request()
        }

    }

}