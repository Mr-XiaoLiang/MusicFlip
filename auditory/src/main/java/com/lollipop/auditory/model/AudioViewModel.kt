package com.lollipop.auditory.model

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lollipop.auditory.data.AlbumInfo
import com.lollipop.auditory.data.ArtistInfo
import com.lollipop.auditory.data.AudioInfo
import com.lollipop.auditory.data.SortOrder
import com.lollipop.auditory.state.AudioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val LocalAudioViewModel = staticCompositionLocalOf<AudioViewModel> {
    error("No ViewModel provided")
}

class AudioViewModel : ViewModel() {

    private val songsSortOrderFlow = MutableStateFlow(SortOrder.Songs.BY_TITLE_ASC)
    private val albumsSortOrderFlow = MutableStateFlow(SortOrder.Albums.BY_TITLE_ASC)
    private val artistsSortOrderFlow = MutableStateFlow(SortOrder.Artists.BY_TITLE_ASC)

    val songsSortOrder = songsSortOrderFlow.asStateFlow()

    val albumsSortOrder = albumsSortOrderFlow.asStateFlow()

    val artistsSortOrder = artistsSortOrderFlow.asStateFlow()

    val songs: StateFlow<List<AudioInfo>> by lazy {
        AudioRepository.audioList
            .combine(songsSortOrderFlow) { songs, order ->
                // 在计算线程进行排序，防止卡顿 UI
                withContext(Dispatchers.Default) {
                    when (order) {
                        SortOrder.Songs.BY_TITLE_ASC -> songs.sortedBy { it.audioName }
                        SortOrder.Songs.BY_TITLE_DESC -> songs.sortedByDescending { it.audioName }
                        SortOrder.Songs.BY_DATE_ADDED_ASC -> songs.sortedBy { it.dateAdded }
                        SortOrder.Songs.BY_DATE_ADDED_DESC -> songs.sortedByDescending { it.dateAdded }
                        SortOrder.Songs.BY_ARTIST_ASC -> songs.sortedBy { it.artist }
                        SortOrder.Songs.BY_ARTIST_DESC -> songs.sortedByDescending { it.artist }
                        SortOrder.Songs.BY_YEAR_ASC -> songs.sortedBy { it.year }
                        SortOrder.Songs.BY_YEAR_DESC -> songs.sortedByDescending { it.year }
                        SortOrder.Songs.BY_RANDOM -> songs.shuffled()
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    val albums: StateFlow<List<AlbumInfo>> by lazy {
        AudioRepository.audioList
            .combine(albumsSortOrderFlow) { songs, order ->
                withContext(Dispatchers.Default) {
                    val albumList = songs.groupBy { it.albumId }
                        .map { (id, list) -> AlbumInfo.build(id, list) }
                    when (order) {
                        SortOrder.Albums.BY_TITLE_ASC -> albumList.sortedBy { it.title }
                        SortOrder.Albums.BY_TITLE_DESC -> albumList.sortedByDescending { it.title }
                        SortOrder.Albums.BY_DATE_ADDED_ASC -> albumList.sortedBy { it.dateAdded }
                        SortOrder.Albums.BY_DATE_ADDED_DESC -> albumList.sortedByDescending { it.dateAdded }
                        SortOrder.Albums.BY_ALBUMS_ARTIST_ASC -> albumList.sortedBy { it.artist }
                        SortOrder.Albums.BY_ALBUMS_ARTIST_DESC -> albumList.sortedByDescending { it.artist }
                        SortOrder.Albums.BY_YEAR_ASC -> albumList.sortedBy { it.year }
                        SortOrder.Albums.BY_YEAR_DESC -> albumList.sortedByDescending { it.year }
                        SortOrder.Albums.BY_RANDOM -> albumList.shuffled()
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    val artists: StateFlow<List<ArtistInfo>> by lazy {
        AudioRepository.audioList
            .combine(artistsSortOrderFlow) { songs, order ->
                withContext(Dispatchers.Default) {
                    val artistList = songs.groupBy { it.artistId }
                        .map { (id, list) -> ArtistInfo.build(id, list) }
                    when (order) {
                        SortOrder.Artists.BY_TITLE_ASC -> artistList.sortedBy { it.name }
                        SortOrder.Artists.BY_TITLE_DESC -> artistList.sortedByDescending { it.name }
                        SortOrder.Artists.BY_RANDOM -> artistList.shuffled()
                    }
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun changeSongsSortOrder(order: SortOrder.Songs) {
        songsSortOrderFlow.value = order
    }

    fun changeAlbumsSortOrder(order: SortOrder.Albums) {
        albumsSortOrderFlow.value = order
    }

    fun changeArtistsSortOrder(order: SortOrder.Artists) {
        artistsSortOrderFlow.value = order
    }

    fun refresh(context: Context) {
        viewModelScope.launch {
            AudioRepository.refreshAudioList(context)
        }
    }

}