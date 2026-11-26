package com.aumomobile.app.reports.ledgers.permanent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumomobile.app.reports.ledgers.LedgerResponse
import com.aumomobile.app.reports.ledgers.LedgerResult
import com.aumomobile.app.reports.ledgers.LedgersApi
import kotlinx.coroutines.launch

// Akun Permanent (Neraca): Assets, Liabilities, Equity.
class GeneralLedgerPermanentViewModel : ViewModel() {
    private val api = LedgersApi()

    var report: LedgerResponse? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var errorMessage: String? by mutableStateOf(null)
        private set

    fun load() {
        if (isLoading) return
        viewModelScope.launch {
            isLoading = true
            when (val result = api.load(isTemporary = false)) {
                is LedgerResult.Success -> {
                    report = result.report
                    errorMessage = null
                }
                is LedgerResult.Failure -> errorMessage = result.message
            }
            isLoading = false
        }
    }
}
