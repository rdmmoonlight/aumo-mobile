package com.aumomobile.app.reports.trialbalances.unadjusted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumomobile.app.network.SingleFlight
import com.aumomobile.app.network.safeBody
import com.aumomobile.app.reports.trialbalances.TrialBalancesApi
import com.aumomobile.app.reports.trialbalances.TrialBalancesReport

// Neraca Saldo (belum disesuaikan): type="unadjusted", hanya jurnal General.
class TrialBalancesViewModel : ViewModel() {
    private val api = TrialBalancesApi()
    private val flight = SingleFlight()

    var report: TrialBalancesReport? by mutableStateOf(null)
        private set

    fun load() {
        flight.launch(viewModelScope) {
            report =
                try {
                    api.getTrialBalances("unadjusted").safeBody<TrialBalancesReport>()
                } catch (t: Throwable) {
                    com.aumomobile.app.crashlog.AppLogger.error("TrialBalancesViewModel", "Error tertangkap", t)
                    null
                }
        }
    }
}
