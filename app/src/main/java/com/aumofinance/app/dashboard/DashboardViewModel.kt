package com.aumomobile.app.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumomobile.app.network.SingleFlight
import com.aumomobile.app.network.safeBody

// State Compose (mutableStateOf), bukan LiveData.
class DashboardViewModel : ViewModel() {
    private val api = DashboardApi()
    private val flight = SingleFlight()

    var summary: DashboardSummary? by mutableStateOf(null)
        private set

    fun load() {
        flight.launch(viewModelScope) {
            summary =
                try {
                    api.getSummary().safeBody<DashboardSummary>()
                } catch (t: Throwable) {
                    com.aumomobile.app.crashlog.AppLogger.error("DashboardViewModel", "Error tertangkap", t)
                    null
                }
        }
    }
}
