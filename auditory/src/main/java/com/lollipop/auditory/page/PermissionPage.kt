package com.lollipop.auditory.page

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lollipop.auditory.R
import com.lollipop.auditory.tool.LocalPermissionLauncher
import com.lollipop.auditory.ui.ContentColumn
import com.lollipop.auditory.ui.PreferencesGroupLazy
import com.lollipop.auditory.ui.PreferencesInfo
import com.lollipop.auditory.ui.PreferencesIntent

@Composable
fun PermissionPage(innerPadding: PaddingValues) {

    val permission = LocalPermissionLauncher.current

    ContentColumn(
        innerPadding = innerPadding,
        showBack = false,
    ) {

        PreferencesGroupLazy {
            PreferencesInfo(
                title = stringResource(R.string.title_permission_read_media_audio),
                info = permission.explanation
            )
        }

        PreferencesGroupLazy {
            PreferencesIntent(
                name = stringResource(R.string.label_request_permission_audio),
                summary = stringResource(R.string.summary_request_permission_audio)
            ) {
                permission.launch()
            }
        }

    }

}