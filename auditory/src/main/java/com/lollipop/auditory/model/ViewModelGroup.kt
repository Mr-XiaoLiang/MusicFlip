package com.lollipop.auditory.model

import androidx.activity.ComponentActivity
import androidx.activity.viewModels

class ViewModelGroup(activity: ComponentActivity) {

    val theme: ThemeViewModel by activity.viewModels()
    val audio: AudioViewModel by activity.viewModels()

}