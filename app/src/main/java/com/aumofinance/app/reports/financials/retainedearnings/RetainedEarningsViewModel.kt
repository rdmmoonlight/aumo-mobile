package com.aumomobile.app.reports.financials.retainedearnings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumomobile.app.network.SingleFlight
import com.aumomobile.app.network.safeBody
import com.aumomobile.app.reports.financials.FinancialsApi
import com.aumomobile.app.reports.financials.RetainedEarningsReport

class RetainedEarningsViewModel : ViewModel() {
    private val api = FinancialsApi()
    private val flight = SingleFlight()

    var report: RetainedEarningsReport? by mutableStateOf(null)
        private set

    fun load() {
        flight.launch(viewModelScope) {
            report =
                try {
                    api.getRetainedEarnings().safeBody<RetainedEarningsReport>()
                } catch (t: Throwable) {
                    com.aumomobile.app.crashlog.AppLogger.error("RetainedEarningsViewModel", "Error tertangkap", t)
                    null
                }
        }
    }
}
