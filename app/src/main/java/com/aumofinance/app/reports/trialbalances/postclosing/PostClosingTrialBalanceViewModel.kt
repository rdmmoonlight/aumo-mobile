package com.aumomobile.app.reports.trialbalances.postclosing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumomobile.app.network.SingleFlight
import com.aumomobile.app.network.safeBody
import com.aumomobile.app.reports.trialbalances.TrialBalancesApi
import com.aumomobile.app.reports.trialbalances.TrialBalancesReport

// Neraca Saldo Pasca-Penutupan: type="post-closing". Retained Earnings sudah
// termasuk efek Closing (walau baris Closing itu sendiri tidak pernah
// tersimpan sebagai entri jurnal).
class PostClosingTrialBalanceViewModel : ViewModel() {
    private val api = TrialBalancesApi()
    private val flight = SingleFlight()

    var report: TrialBalancesReport? by mutableStateOf(null)
        private set

    fun load() {
        flight.launch(viewModelScope) {
            report =
                try {
                    api.getTrialBalances("post-closing").safeBody<TrialBalancesReport>()
                } catch (t: Throwable) {
                    com.aumomobile.app.crashlog.AppLogger.error("PostClosingTrialBalanceViewModel", "Error tertangkap", t)
                    null
                }
        }
    }
}
