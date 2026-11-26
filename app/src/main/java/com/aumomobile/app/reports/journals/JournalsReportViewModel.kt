package com.aumomobile.app.reports.journals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aumomobile.app.network.SingleFlight
import com.aumomobile.app.network.safeBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive

// State Compose (mutableStateOf), bukan LiveData (lihat
// JournalReportScreen.kt). Satu ViewModel dipakai baik oleh General maupun
// Adjusting Journal — loadGeneral()/loadAdjusting() beda endpoint.
class JournalReportViewModel : ViewModel() {
    private val reportApi = JournalReportApi()
    private val flight = SingleFlight()

    var entries: List<JournalReportEntry> by mutableStateOf(emptyList())
        private set
    var selectedPeriodName: String? by mutableStateOf(null)
        private set
    var snackbarMessage: String? by mutableStateOf(null)
        private set

    fun loadGeneral() {
        fetch { reportApi.getGeneralJournal() }
    }

    fun loadAdjusting() {
        fetch { reportApi.getAdjustingJournal() }
    }

    private fun fetch(call: suspend () -> HttpResponse) {
        flight.launch(viewModelScope) {
            try {
                val response = call()
                val body = runCatching { response.safeBody<JournalReportResponse>() }.getOrNull()
                // runCatching ikut menelan CancellationException; pastikan pembatalan tetap berlaku.
                ensureActive()
                if (!response.status.isSuccess() || body == null || !body.success) {
                    entries = emptyList()
                    snackbarMessage = body?.message?.takeIf { it.isNotBlank() }
                        ?: "Failed to load journal (${response.status.value})."
                    return@launch
                }
                entries = body.entries.orEmpty()
                selectedPeriodName = body.selectedPeriodName
            } catch (c: CancellationException) {
                throw c
            } catch (t: Throwable) {
                com.aumomobile.app.crashlog.AppLogger.error("JournalReportViewModel", "Error tertangkap", t)
                entries = emptyList()
                snackbarMessage = t.message ?: "Connection failed."
            }
        }
    }

    fun clearSnackbar() {
        snackbarMessage = null
    }
}
