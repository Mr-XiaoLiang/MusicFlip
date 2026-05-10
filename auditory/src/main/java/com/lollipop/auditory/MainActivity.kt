package com.lollipop.auditory

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.lollipop.auditory.base.AuditoryBasicActivity
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.databinding.ActivityMainBinding
import com.lollipop.auditory.main.BasicSheetPanel
import com.lollipop.auditory.main.FullScreenPlayerPanel
import com.lollipop.auditory.main.MiniSheetPlayerPanel
import com.lollipop.auditory.model.AudioViewModel
import com.lollipop.auditory.model.LocalAudioViewModel
import com.lollipop.auditory.model.ThemeViewModel
import com.lollipop.auditory.ui.MediaFlowTheme
import com.lollipop.common.ui.page.PageOrientation
import com.lollipop.common.ui.view.BlurHelper
import kotlinx.coroutines.launch

class MainActivity : AuditoryBasicActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val fullScreenPlayerPanel by lazy {
        FullScreenPlayerPanel(binding.playerFullPanel)
    }

    private val miniSheetPlayerPanel by lazy {
        MiniSheetPlayerPanel(binding.playerMiniPanel)
    }

    private val sheetPanel by lazy {
        SheetPanelGroup(
            arrayOf(
                miniSheetPlayerPanel,
                fullScreenPlayerPanel
            )
        )
    }

    private val themeModel: ThemeViewModel by viewModels()
    private val audioModel: AudioViewModel by viewModels()

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.playerSheet)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.contentView.addView(
            createContentView(),
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        log.i("onCreate")
        registerInsets()
        rememberState()
        updateBlur()
        sheetPanel.onCreate()
        if (audioModel.songs.value.isEmpty()) {
            log.i("onCreate.refresh")
            audioModel.refresh(this)
        }
    }

    private fun registerInsets() {
        registerGuidelineInsetsListener(sheetPanel.guidelineInsetsHelper)
    }

    private fun rememberState() {
        log.i("rememberState")
        // 观察数据变化
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                themeModel.playingInfo.collect { audioInfo ->
                    onAudioInfoChanged(audioInfo)
                }
            }
        }

        miniSheetPlayerPanel.onSizeChanged { _, height ->
            onPlayerPeekHeightChangedPx(height)
        }
        bottomSheetBehavior.addBottomSheetCallback(sheetPanel.bottomSheetCallback)
    }

    private fun onAudioInfoChanged(audioInfo: AudioInfo?) {
        log.i("onAudioInfoChanged = $audioInfo")
        // TODO()
    }

    private fun onPlayerPeekHeightChangedPx(height: Int) {
        log.i("onPlayerPeekHeightChangedPx = $height")
        themeModel.playerPeekHeight = height
        bottomSheetBehavior.peekHeight = height
    }

    private fun createContentView(): View {
        log.i("createContentView")
        return ComposeView(this).apply {
            setContent {
                val playerPeekHeight = remember { themeModel.playerPeekHeight }
                MediaFlowTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val currentDensity = LocalDensity.current
                        val currentDirection = LocalLayoutDirection.current
                        // 确保发生变化的时候，Insets 会被重新计算
                        val finalPadding = remember(
                            innerPadding,
                            playerPeekHeight,
                            currentDensity,
                            currentDirection
                        ) {
                            val playerPeekDp = with(currentDensity) { playerPeekHeight.toDp() }
                            PaddingValues(
                                top = innerPadding.calculateTopPadding(),
                                bottom = max(playerPeekDp, innerPadding.calculateBottomPadding()),
                                start = innerPadding.calculateStartPadding(currentDirection),
                                end = innerPadding.calculateEndPadding(currentDirection)
                            )
                        }
                        CompositionLocalProvider(LocalAudioViewModel provides audioModel) {
                            Content(finalPadding)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(innerPadding: PaddingValues) {
        // TODO("这里是 Compose 内容")
        val viewModel = LocalAudioViewModel.current // 获取ViewModel
        val songs by viewModel.songs.collectAsStateWithLifecycle() // 获取歌曲列表
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(0xFF000000))
        ) {
            items(songs, key = { it.id }) { song ->
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(song.title)
                }
            }
        }
    }

    private fun updateBlur() {
        BlurHelper.bind(
            window,
            binding.blurTarget,
            binding.playerFullPanel.playButtonBlur,
        )
    }

    override fun onResume() {
        super.onResume()
        sheetPanel.onResume()
    }

    override fun onPause() {
        super.onPause()
        sheetPanel.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateBlur()
    }

    override fun onOrientationChanged(orientation: PageOrientation) {
        super.onOrientationChanged(orientation)
        sheetPanel.onOrientationChanged(orientation)
    }

    override fun onWindowInsetsChanged(
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
    }

    private class SheetPanelGroup(
        val panels: Array<BasicSheetPanel>
    ) : BasicSheetPanel() {

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        onExpand()
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        onCollapse()
                    }

                    else -> {}
                }
            }

            override fun onSlide(view: View, offset: Float) {
                onSlide(offset)
            }
        }

        private fun invoke(block: (BasicSheetPanel) -> Unit) {
            panels.forEach(block)
        }

        override fun onCreate() {
            invoke { it.onCreate() }
        }

        override fun onGuidelineInsetsChanged(
            left: Int, top: Int, right: Int, bottom: Int
        ) {
            invoke { it.guidelineInsetsHelper.updateGuidelineInsets(left, top, right, bottom) }
        }

        override fun onSlide(offset: Float) {
            invoke { it.onSlide(offset) }
        }

        override fun onExpand() {
            invoke { it.onExpand() }
        }

        override fun onCollapse() {
            invoke { it.onCollapse() }
        }

        override fun onResume() {
            invoke { it.onResume() }
        }

        override fun onPause() {
            invoke { it.onPause() }
        }

        override fun onOrientationChanged(orientation: PageOrientation) {
            invoke { it.onOrientationChanged(orientation) }
        }

    }

}