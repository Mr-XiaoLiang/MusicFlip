package com.lollipop.auditory

import android.app.Application
import com.lollipop.common.tools.LLog
import com.lollipop.common.tools.LLog.Companion.registerLog

class LApplication: Application() {

    private val log by lazy {
        registerLog()
    }

    override fun onCreate() {
        super.onCreate()
        LLog.isDebug = BuildConfig.DEBUG
        log.i("onCreate")
    }

}