package com.aumofinance.app.reports.journals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.aumofinance.app.ui.components.SnackbarMessageHost
import com.aumofinance.app.ui.theme.AumoTheme

// Sama seperti General Journal, tapi backend sudah memfilter journalType
// "Adjusting" saja. Halaman hanya menampilkan data (tanpa edit/hapus).
// Host Compose tipis — tampilan ada di JournalReportScreen.kt, state ada di
// JournalReportViewModel.
class AdjustingJournalReportActivity : ComponentActivity() {
    private val viewModel: JournalReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AumoTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    JournalReportScreen(
                        entries = viewModel.entries,
                        selectedPeriodName = viewModel.selectedPeriodName,
                        defaultPeriodLabel = "Belum ada periode dipilih",
                    )
                    SnackbarMessageHost(
                        message = viewModel.snackbarMessage,
                        onConsumed = { viewModel.clearSnackbar() },
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAdjusting()
    }
}
