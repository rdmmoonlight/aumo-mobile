package com.aumomobile.app.core

import android.app.Application
import com.aumomobile.app.crashlog.AppLogger
import com.aumomobile.app.crashlog.CrashLogHandler
import com.aumomobile.app.network.SessionExpiredHandler
import com.aumomobile.app.network.SessionStore

// CrashLogHandler sebelumnya ada di codebase tapi TIDAK PERNAH benar-benar
// didaftarkan sebagai default uncaught exception handler — dead code sejak
// dibuat. Didaftarkan di sini supaya CrashLogActivity benar-benar punya isi.
class AumoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLogger.init(this)
        Thread.setDefaultUncaughtExceptionHandler(CrashLogHandler())
        SessionStore.init(this)
        SessionExpiredHandler.init(this)
    }
}
