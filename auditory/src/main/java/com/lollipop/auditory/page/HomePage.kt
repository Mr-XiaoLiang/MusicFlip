package com.lollipop.auditory.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lollipop.auditory.audio.AudioServiceHelper
import com.lollipop.auditory.audio.LocalAudioController
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.model.LocalAudioViewModel
import com.lollipop.auditory.router.DetailPane
import com.lollipop.auditory.router.DetailPaneRouter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomePage(innerPadding: PaddingValues) {
    val viewModel = LocalAudioViewModel.current // 获取ViewModel
    val songs by viewModel.songs.collectAsStateWithLifecycle() // 获取歌曲列表
    val layoutDirection = LocalLayoutDirection.current
    val paddingStart = innerPadding.calculateStartPadding(layoutDirection)
    val paddingEnd = innerPadding.calculateEndPadding(layoutDirection)
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(innerPadding.calculateTopPadding()))
        }
        item {
            HomeHeader(paddingStart, paddingEnd)
        }
        item {
            HomeToday(paddingStart, paddingEnd)
        }
        item {
            HomeAlbum(paddingStart, paddingEnd)
        }
        item {
            HomeArtists(paddingStart, paddingEnd)
        }
        items(songs, key = { it.id }) { song ->
            HomeSongItem(song, paddingStart, paddingEnd)
        }
        item {
            Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
        }
    }
}

/**
 * 首页的头部
 */
@Composable
private fun HomeHeader(paddingStart: Dp, paddingEnd: Dp) {
    val router = DetailPaneRouter.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = paddingStart, end = paddingEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(com.lollipop.auditory.R.string.home_title),
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                router.navigateTo(DetailPane.Settings)
                            }
                        }
                    )
                    .rotate(90F),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * 首页的今日推荐
 */
@Composable
private fun HomeToday(paddingStart: Dp, paddingEnd: Dp) {
    // TODO
}

/**
 * 首页的专辑
 */
@Composable
private fun HomeAlbum(paddingStart: Dp, paddingEnd: Dp) {
    // TODO
}

/**
 * 首页的歌手
 */
@Composable
private fun HomeArtists(paddingStart: Dp, paddingEnd: Dp) {
    // TODO
}

@Composable
private fun HomeSongItem(song: AudioInfo, paddingStart: Dp, paddingEnd: Dp) {
    val controller = LocalAudioController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd)
            .padding(16.dp)
            .background(Color(0x33FF0000), shape = MaterialTheme.shapes.large)
            .clickable {
                controller.option {
                    it.setMediaItem(AudioServiceHelper.toMediaItem(song))
                    it.prepare()
                    it.play()
                }
//                        coroutineScope.launch {
//                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, DetailPane.Empty)
//                        }
            }
            .padding(16.dp)
    ) {
        Text(song.displayName, fontSize = 16.sp)
        Text(song.artist, fontSize = 12.sp)
        Text(song.album, fontSize = 12.sp)
    }
}
