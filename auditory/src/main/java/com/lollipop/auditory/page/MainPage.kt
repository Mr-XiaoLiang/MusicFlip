package com.lollipop.auditory.page

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.lollipop.auditory.page.detail.AboutDetail
import com.lollipop.auditory.page.detail.AlbumDetail
import com.lollipop.auditory.page.detail.ArtistsDetail
import com.lollipop.auditory.page.detail.EmptyDetail
import com.lollipop.auditory.page.detail.SettingsDetail
import com.lollipop.auditory.page.detail.SongsDetail
import com.lollipop.auditory.page.detail.TodayDetail
import com.lollipop.auditory.router.DetailAnimatedPaneScope
import com.lollipop.auditory.router.DetailPane
import com.lollipop.auditory.router.DetailPaneNavigator
import com.lollipop.auditory.router.DetailPaneRouter
import com.lollipop.auditory.router.DetailSharedPaneScope

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainPage(innerPadding: PaddingValues) {

    val navigator = rememberListDetailPaneScaffoldNavigator<DetailPane>()
    val detailRouter = remember { DetailPaneNavigator(navigator) }
    val paneExpansionState = rememberPaneExpansionState()

    SharedTransitionLayout {
        NavigableListDetailPaneScaffold(
            navigator = navigator,
            paneExpansionState = paneExpansionState,
            paneExpansionDragHandle = {
                val interactionSource = remember { MutableInteractionSource() }
                VerticalDragHandle(
                    modifier = Modifier.paneExpansionDraggable(
                        state = paneExpansionState,
                        minTouchTargetSize = LocalMinimumInteractiveComponentSize.current,
                        interactionSource = interactionSource
                    ),
                    interactionSource = interactionSource
                )
            },
            defaultBackBehavior = BackNavigationBehavior.PopLatest,
            listPane = {
                AnimatedPane {
                    CompositionLocalProvider(
                        DetailPaneRouter provides detailRouter,
                        DetailSharedPaneScope provides this@SharedTransitionLayout,
                        DetailAnimatedPaneScope provides this@AnimatedPane
                    ) {
                        HomePage(innerPadding = innerPadding)
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    CompositionLocalProvider(
                        DetailPaneRouter provides detailRouter,
                        DetailSharedPaneScope provides this@SharedTransitionLayout,
                        DetailAnimatedPaneScope provides this@AnimatedPane
                    ) {
                        DetailPaneDispatcher(innerPadding = innerPadding)
                    }
                }
            }
        )
    }

}

//with(sharedTransitionScope) {
//    Image(
//        painter = painterResource(id = word.icon),
//        contentDescription = word.word,
//        modifier = Modifier
//            .padding(horizontal = 8.dp)
//            .sharedElement(
//                rememberSharedContentState(key = "image-${word.word}"),
//                animatedVisibilityScope = animatedVisibilityScope,
//            )
//    )
//}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun DetailPaneDispatcher(
    innerPadding: PaddingValues,
) {
    val paneKey = DetailPaneRouter.current.current()
    when (paneKey) {
        DetailPane.Empty -> {
            EmptyDetail(innerPadding = innerPadding)
        }

        DetailPane.Settings -> {
            SettingsDetail(innerPadding = innerPadding)
        }

        DetailPane.Today -> {
            TodayDetail(innerPadding = innerPadding)
        }

        DetailPane.Album -> {
            AlbumDetail(innerPadding = innerPadding)
        }

        DetailPane.Artists -> {
            ArtistsDetail(innerPadding = innerPadding)
        }

        DetailPane.Songs -> {
            SongsDetail(innerPadding = innerPadding)
        }

        DetailPane.About -> {
            AboutDetail(innerPadding = innerPadding)
        }
    }
}