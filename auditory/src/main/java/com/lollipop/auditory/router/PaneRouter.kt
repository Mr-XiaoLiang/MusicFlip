package com.lollipop.auditory.router

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.staticCompositionLocalOf

val DetailPaneRouter = staticCompositionLocalOf<DetailPaneNavigator> {
    error("No Navigator provided")
}

val DetailSharedPaneScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("No SharedScope provided")
}

val DetailAnimatedPaneScope = staticCompositionLocalOf<AnimatedVisibilityScope> {
    error("No SharedScope provided")
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class DetailPaneNavigator(
    val navigator: ThreePaneScaffoldNavigator<DetailPane>
) {

    fun current(def: DetailPane = DetailPane.Empty): DetailPane {
        return navigator.currentDestination?.contentKey ?: def
    }

    suspend fun navigateTo(contentKey: DetailPane) {
        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, contentKey)
    }

    fun canNavigateBack(
        backNavigationBehavior: BackNavigationBehavior =
            BackNavigationBehavior.PopUntilScaffoldValueChange
    ): Boolean {
        return navigator.canNavigateBack(backNavigationBehavior)
    }

    suspend fun navigateBack(
        backNavigationBehavior: BackNavigationBehavior =
            BackNavigationBehavior.PopUntilScaffoldValueChange
    ): Boolean {
        return navigator.navigateBack(backNavigationBehavior)
    }

}
